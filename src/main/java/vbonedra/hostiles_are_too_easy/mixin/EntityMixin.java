package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

import static vbonedra.hostiles_are_too_easy.util.ICelestialType.celestialTypeVanilla;

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

            if (celestialType == celestialTypeVanilla || damage_source == null) return;

            Block associatedBlock = Block.getBlock(celestialType);
            if (associatedBlock == null) return;

            ItemStack item_stack = damage_source.getItemAttackedWith();
            if (item_stack != null && item_stack.getItem() instanceof ItemTool) {
                cir.setReturnValue(!item_stack.getItemAsTool().isEffectiveAgainstBlock(associatedBlock, 0));
                return;
            }

            cir.setReturnValue(true);
        }
        else if (entity instanceof EntityCreeper) {
            cir.setReturnValue(damage_source.isExplosion());
        }
    }

}
