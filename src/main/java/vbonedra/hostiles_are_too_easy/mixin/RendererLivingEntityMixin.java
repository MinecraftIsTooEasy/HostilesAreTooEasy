package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {

    @ModifyVariable(method = "renderModelGlowing(Lnet/minecraft/EntityLivingBase;FFFFFF)V", at = @At("STORE"), ordinal = 0)
    private ResourceLocation overrideGlowTextureForBoneLord(ResourceLocation original, EntityLivingBase par1EntityLivingBase) {
        if (par1EntityLivingBase instanceof EntitySkeleton skeleton) {
            if (skeleton.isBoneLord()) return new ResourceLocation("textures/entity/skeleton/bone_lord_glow.png");
            if (skeleton.isLongdeadGuardian()) return new ResourceLocation("textures/entity/skeleton/longdead_guardian_glow.png");
        }
        return original;
    }

}
