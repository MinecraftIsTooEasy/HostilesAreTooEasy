package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.cacheResourceLocation;

@Mixin(RendererLivingEntity.class)
public abstract class RendererLivingEntityMixin {
    @Unique private static final ResourceLocation BONE_LORD_GLOW = new ResourceLocation("textures/entity/skeleton/bone_lord_glow.png");
    @Unique private static final ResourceLocation LONGDEAD_GUARDIAN_GLOW = new ResourceLocation("textures/entity/skeleton/longdead_guardian_glow.png");
    @Unique private static final ResourceLocation REVENANT_GLOW = new ResourceLocation("textures/entity/zombie/revenant_glow.png");
    @Unique private static final ResourceLocation PHASE_SPIDER_GLOW = new ResourceLocation("textures/entity/spider/phase_spider_glow.png");

    @Unique private static final ResourceLocation SQUID_MASK = new ResourceLocation("textures/blocks/wool_colored_cyan.png");
    @Unique private static final ResourceLocation SQUID_BASE = new ResourceLocation("textures/entity/squid.png");
    @Unique private static final ResourceLocation GHOUL_SHADOW_MASK = new ResourceLocation("textures/entity/zombie/revenant_glow.png");
    @Unique private static final ResourceLocation GHOUL_SHADOW_BASE = new ResourceLocation("textures/entity/earth_elemental/earth_elemental_glow.png");

    @Unique private static final String SQUID_CACHE_KEY = "squid_glow";
    @Unique private static final String GHOUL_CACHE_KEY = "ghoul_vampire_glow";
    @Unique private static final String SHADOW_CACHE_KEY = "shadow_gloom_glow";

    @ModifyVariable(method = "renderModelGlowing(Lnet/minecraft/EntityLivingBase;FFFFFF)V", at = @At(value = "STORE"), ordinal = 0)
    private ResourceLocation renderModelGlowing_replaceForCustomTextures(ResourceLocation glowing_texture, EntityLivingBase par1EntityLivingBase) throws IOException {
        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityLivingBase);

        if (par1EntityLivingBase instanceof EntitySkeleton skeleton) {
            if (skeleton.isBoneLord()) {
                return BONE_LORD_GLOW;
            }
            if (skeleton.isLongdeadGuardian()) {
                return LONGDEAD_GUARDIAN_GLOW;
            }
        }
        if (par1EntityLivingBase instanceof EntityZombie zombie) {
            if (zombie.isRevenant()) {
                return REVENANT_GLOW;
            }
        }
        if (par1EntityLivingBase instanceof EntityArachnid) {
            if (par1EntityLivingBase.getClass() == EntityPhaseSpider.class) {
                return PHASE_SPIDER_GLOW;
            }
        }
        if (par1EntityLivingBase instanceof EntitySquid) {
            if (hasCelestialType(celestialType, CelestialSquid.GLOW.id)) {
                if (cacheResourceLocation.containsKey(SQUID_CACHE_KEY)) {
                    return cacheResourceLocation.get(SQUID_CACHE_KEY);
                }

                Minecraft mc = Minecraft.getMinecraft();
                ResourceLocation dynamicResource = TexturePacker.getOrCreateInMemoryTexture(
                        SQUID_BASE.getResourcePath(),
                        SQUID_CACHE_KEY,
                        (baseImg) -> {
                            BufferedImage maskImg = ImageIO.read(mc.getResourceManager().getResource(SQUID_MASK).getInputStream());
                            return TexturePacker.maskTemplateWithPixelSourceByBrightness(
                                    maskImg,
                                    baseImg,
                                    1.0F,
                                    1.0F,
                                    -1,
                                    false
                            );
                        }
                );

                if (dynamicResource != null) return dynamicResource;
            }
        }
        if (par1EntityLivingBase instanceof EntityGhoul) {
            if (hasCelestialType(celestialType, CelestialGhoul.VAMPIRIC.id)) {
                if (cacheResourceLocation.containsKey(GHOUL_CACHE_KEY)) {
                    return cacheResourceLocation.get(GHOUL_CACHE_KEY);
                }

                Minecraft mc = Minecraft.getMinecraft();
                ResourceLocation dynamicResource = TexturePacker.getOrCreateInMemoryTexture(
                        GHOUL_SHADOW_BASE.getResourcePath(),
                        GHOUL_CACHE_KEY,
                        (baseImg) -> {
                            BufferedImage maskImg = ImageIO.read(mc.getResourceManager().getResource(GHOUL_SHADOW_MASK).getInputStream());
                            return TexturePacker.maskTemplateWithPixelSourceByBrightness(
                                    maskImg,
                                    baseImg,
                                    1.0F,
                                    0.0F,
                                    -1,
                                    true
                            );
                        }
                );

                if (dynamicResource != null) return dynamicResource;
            }
        }
        if (par1EntityLivingBase instanceof EntityShadow) {
            if (hasCelestialType(celestialType, CelestialShadow.GLOOM.id)) {
                if (cacheResourceLocation.containsKey(SHADOW_CACHE_KEY)) {
                    return cacheResourceLocation.get(SHADOW_CACHE_KEY);
                }

                Minecraft mc = Minecraft.getMinecraft();
                ResourceLocation dynamicResource = TexturePacker.getOrCreateInMemoryTexture(
                        GHOUL_SHADOW_BASE.getResourcePath(),
                        SHADOW_CACHE_KEY,
                        (baseImg) -> {
                            BufferedImage maskImg = ImageIO.read(mc.getResourceManager().getResource(GHOUL_SHADOW_MASK).getInputStream());
                            return TexturePacker.maskTemplateWithPixelSourceByBrightness(
                                    maskImg,
                                    baseImg,
                                    1.0F,
                                    0.0F,
                                    -1,
                                    true
                            );
                        }
                );

                if (dynamicResource != null) return dynamicResource;
            }
        }

        return glowing_texture;
    }
}
