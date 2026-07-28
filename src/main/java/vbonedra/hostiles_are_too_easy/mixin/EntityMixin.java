package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
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
                cir.setReturnValue(!item_stack.getItemAsTool().isEffectiveAgainstBlock(associatedBlock, 0) || damage_source.isExplosion());
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
    private void getCustomCelestialEntityName(CallbackInfoReturnable<String> cir) {
        Object entity = this;

        if (!(entity instanceof ICelestialType)) {
            return;
        }

        int celestialType = ((ICelestialType) entity).HATE$getCelestialType();
        if (celestialType <= 0) {
            return;
        }

        if (entity instanceof EntityIronGolem) {
            if (Block.blocksList[celestialType] != null) {
                String blockName = Block.blocksList[celestialType].getLocalizedName();
                String originalName = cir.getReturnValue();
                cir.setReturnValue(blockName + " " + originalName);
            }
            return;
        }

        String originalName = cir.getReturnValue();
        String langKey = getString(entity, celestialType);

        if (langKey != null) {
            String translatedName = StatCollector.translateToLocal(langKey);
            if (!translatedName.equals(langKey)) {
                cir.setReturnValue(translatedName + " " + originalName);
            }
        }
    }

    @Unique
    private static @Nullable String getString(Object entity, int celestialType) {
        String langKey = null;

        if (entity instanceof EntityPhaseSpider) {
            if (celestialType == 1) {
                langKey = "entity.arachnid_warp.name";
            }
        } else if (entity instanceof EntityCreeper) {
            if (celestialType == 1) {
                langKey = "entity.creeper_mimic.name";
            }
        } else if (entity instanceof EntitySkeleton) {
            if (celestialType == 1) {
                langKey = "entity.skeleton_withered.name";
            }
        } else if (entity instanceof EntityZombie) {
            if (celestialType == 1) {
                langKey = "entity.zombie_phase.name";
            }
        } else if (entity instanceof EntityGhoul) {
            if (celestialType == 1) {
                langKey = "entity.ghoul_vampire.name";
            }
        }
        return langKey;
    }

}
