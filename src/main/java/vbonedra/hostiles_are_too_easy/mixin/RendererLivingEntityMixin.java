package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {
    // that's terrible, if other mods add extend entity class with custom glow texture, it would be overridden with this. mixin to Render.class or change how custom texture applied
    @ModifyVariable(method = "renderModelGlowing(Lnet/minecraft/EntityLivingBase;FFFFFF)V", at = @At("STORE"), ordinal = 0)
    private ResourceLocation renderModelGlowing(ResourceLocation original, EntityLivingBase par1EntityLivingBase) {
        if (par1EntityLivingBase instanceof EntitySkeleton skeleton) {
            if (skeleton.isBoneLord()) return new ResourceLocation("textures/entity/skeleton/bone_lord_glow.png");
            if (skeleton.isLongdeadGuardian()) return new ResourceLocation("textures/entity/skeleton/longdead_guardian_glow.png");
        }
        if (par1EntityLivingBase instanceof EntityArachnid spider) {
            if (spider.getClass() == EntityBlackWidowSpider.class) return new ResourceLocation("textures/entity/spider/black_widow_glow.png");
            if (spider.getClass() == EntityCaveSpider.class) return new ResourceLocation("textures/entity/spider/cave_spider_glow.png");
            if (spider.getClass() == EntityDemonSpider.class) return new ResourceLocation("textures/entity/spider/demon_spider_glow.png");
            if (spider.getClass() == EntityPhaseSpider.class) return new ResourceLocation("textures/entity/spider/phase_spider_glow.png");
            if (spider.getClass() == EntitySpider.class) return new ResourceLocation("textures/entity/spider/spider_glow.png");
            if (spider.getClass() == EntityWoodSpider.class) return new ResourceLocation("textures/entity/spider/wood_spider_glow.png");
        }
        return original;
    }

}
