package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {
    // that's terrible, if other mods add extend entity class with custom glow texture, it would be overridden without this applying. maybe mixin to Render.class or change how custom texture applied would be better
    // TODO: check for celestial type
    @ModifyVariable(method = "renderModelGlowing(Lnet/minecraft/EntityLivingBase;FFFFFF)V", at = @At("STORE"), ordinal = 0)
    private ResourceLocation renderModelGlowing_replaceForCustomTextures(ResourceLocation original, EntityLivingBase par1EntityLivingBase) {
        if (par1EntityLivingBase instanceof EntitySkeleton skeleton) {
            if (skeleton.isBoneLord()) return new ResourceLocation("textures/entity/skeleton/bone_lord_glow.png");
            if (skeleton.isLongdeadGuardian()) return new ResourceLocation("textures/entity/skeleton/longdead_guardian_glow.png");
        }
        if (par1EntityLivingBase instanceof EntityZombie zombie) {
            if (zombie.isRevenant()) return new ResourceLocation("textures/entity/zombie/revenant_glow.png");
        }
        if (par1EntityLivingBase instanceof EntityArachnid spider) {
            if (spider.getClass() == EntityPhaseSpider.class) return new ResourceLocation("textures/entity/spider/phase_spider_glow.png");
        }
        return original;
    }

}
