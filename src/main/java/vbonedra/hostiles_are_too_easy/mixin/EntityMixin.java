package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType.*;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "isImmuneTo(Lnet/minecraft/DamageSource;)Z", at = @At("RETURN"), cancellable = true)
    private void isImmuneTo(DamageSource damage_source, CallbackInfoReturnable<Boolean> cir) {
        Object entity = this;
        int celestialType = celestialTypeVanilla;
        if (entity instanceof ICelestialType celestial) {
            celestialType = celestial.HATE$getCelestialType();
        }
        if (entity instanceof EntitySilverfish) {

            if (celestialType == celestialTypeVanilla || celestialType == celestialTypeUnset || damage_source == null) return;

            Block block = Block.getBlock(celestialType);
            ItemStack item_stack = damage_source.getItemAttackedWith();
            if (block == null) return;

            if (damage_source == DamageSource.fall) {
                return;
            } else if (damage_source.isMelee() && damage_source.getResponsibleEntity() instanceof EntityIronGolem) {
                return;
            } else if (item_stack != null && item_stack.getItem() instanceof ItemTool) {
                cir.setReturnValue(!item_stack.getItemAsTool().isEffectiveAgainstBlock(block, 0) || damage_source.isExplosion());
                return;
            }

            cir.setReturnValue(true);
        }
        else if (entity instanceof EntityCreeper) {
            cir.setReturnValue(damage_source.isExplosion());
        }
        else if (entity instanceof EntityWither) {
            cir.setReturnValue(damage_source.isExplosion());
        }
    }


    @Inject(method = "getEntityName()Ljava/lang/String;", at = @At("RETURN"), cancellable = true)
    private void getEntityName(CallbackInfoReturnable<String> cir) {
        Object entity = this;

        if (!(entity instanceof ICelestialType)) {
            return;
        }

        int celestialType = ((ICelestialType) entity).HATE$getCelestialType();
        String name = cir.getReturnValue();

        if (entity instanceof EntityIronGolem) {
            if (Block.blocksList[celestialType] != null) {
                cir.setReturnValue(Block.blocksList[celestialType].getLocalizedName() + " " + name);
            }
        }
        if (entity instanceof EntitySilverfish) {
            if (Block.blocksList[celestialType] != null) {
                cir.setReturnValue(Block.blocksList[celestialType].getLocalizedName() + " " + name);
            }
        }

        if (celestialType <= 0) {
            return;
        }


        cir.setReturnValue(getPrefix(entity, celestialType) + name);

    }

}
