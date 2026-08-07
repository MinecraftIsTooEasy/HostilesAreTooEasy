package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

import java.util.HashMap;
import java.util.Map;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType.*;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {

    @Unique
    private final Map<String, ResourceLocation> blendedGlowCache = new HashMap<>();

    @ModifyVariable(method = "renderModelGlowing(Lnet/minecraft/EntityLivingBase;FFFFFF)V", at = @At(value = "STORE"), ordinal = 0)
    private ResourceLocation renderModelGlowing_replaceForCustomTextures(ResourceLocation glowing_texture, EntityLivingBase par1EntityLivingBase) {
        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityLivingBase);

        if (par1EntityLivingBase instanceof EntitySkeleton skeleton) {
            if (skeleton.isBoneLord()) {
                return new ResourceLocation("textures/entity/skeleton/bone_lord_glow.png");
            }
            if (skeleton.isLongdeadGuardian()) {
                return new ResourceLocation("textures/entity/skeleton/longdead_guardian_glow.png");
            }
        }
        if (par1EntityLivingBase instanceof EntityZombie zombie) {
            if (zombie.isRevenant()) {
                return new ResourceLocation("textures/entity/zombie/revenant_glow.png");
            }
        }
        if (par1EntityLivingBase instanceof EntityArachnid) {
            if (par1EntityLivingBase.getClass() == EntityPhaseSpider.class) {
                return new ResourceLocation("textures/entity/spider/phase_spider_glow.png");
            }
        }
        if (par1EntityLivingBase instanceof EntitySquid) {
            if (celestialType == celestialTypeSquidGlow) {
                String cacheKey = "squid_glow";
                if (!blendedGlowCache.containsKey(cacheKey)) {
                    ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                            "textures/blocks/wool_colored_cyan.png",
                            "textures/entity/squid.png",
                            1.0F,
                            1.0F,
                            false
                    );
                    blendedGlowCache.put(cacheKey, finalBlendedTexture);
                }
                return blendedGlowCache.get(cacheKey);
            }
        }
        if (par1EntityLivingBase instanceof EntityGhoul) {
            if (celestialType == celestialTypeGhoulVampire) {
                String cacheKey = "ghoul_vampire_glow";
                if (!blendedGlowCache.containsKey(cacheKey)) {
                    ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                            "textures/entity/zombie/revenant_glow.png",
                            "textures/entity/earth_elemental/earth_elemental_glow.png",
                            1.0F,
                            0.0F,
                            true
                    );
                    blendedGlowCache.put(cacheKey, finalBlendedTexture);
                }
                return blendedGlowCache.get(cacheKey);
            }
        }
        if (par1EntityLivingBase instanceof EntityShadow) {
            if (celestialType == celestialTypeShadowGloom) {
                String cacheKey = "shadow_gloom_glow";
                if (!blendedGlowCache.containsKey(cacheKey)) {
                    ResourceLocation finalBlendedTexture = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                            "textures/entity/zombie/revenant_glow.png",
                            "textures/entity/earth_elemental/earth_elemental_glow.png",
                            1.0F,
                            0.0F,
                            true
                    );
                    blendedGlowCache.put(cacheKey, finalBlendedTexture);
                }
                return blendedGlowCache.get(cacheKey);
            }
        }

        return glowing_texture;
    }
}
