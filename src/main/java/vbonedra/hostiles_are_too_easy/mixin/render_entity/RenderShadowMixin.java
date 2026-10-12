package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import moddedmite.rustedironcore.network.Network;
import vbonedra.hostiles_are_too_easy.network.C2SRequestCelestialType;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeCache;
import vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.cacheResourceLocation;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;

@Mixin(RenderShadow.class)
public abstract class RenderShadowMixin {
    @Unique private static final ResourceLocation GLOOM_RL = new ResourceLocation("textures/blocks/wool_colored_magenta.png");
    @Unique private static final ResourceLocation SPECTRAL_RL = new ResourceLocation("textures/blocks/wool_colored_yellow.png");

    @Inject(method = "getEntityTexture(Lnet/minecraft/Entity;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getEntityTexture(Entity par1Entity, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (!(par1Entity instanceof EntityShadow shadow)) return;
        if (shadow.worldObj == null) return;

        int entityId = shadow.entityId;

        if (!CelestialTypeCache.clientCelestialTypeMap.containsKey(entityId)) {
            if (!CelestialTypeCache.requestedEntities.contains(entityId) && shadow.worldObj.isRemote) {
                Network.sendToServer(new C2SRequestCelestialType(entityId));
                CelestialTypeCache.requestedEntities.add(entityId);
            }
            return;
        }

        Integer typeObject = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
        if (typeObject == null) {
            return;
        }

        int celestialType = CelestialTypeGetter.getCelestialType(shadow);
        String templateTexture = cir.getReturnValue().getResourcePath();
        Minecraft mc = Minecraft.getMinecraft();

        if (hasCelestialType(celestialType, CelestialTypeData.CelestialShadow.GLOOM.id)) {
            String cacheKeyFinal = new StringBuilder(templateTexture)
                    .append("_")
                    .append(GLOOM_RL.getResourcePath())
                    .toString();

            if (cacheResourceLocation.containsKey(cacheKeyFinal)) {
                cir.setReturnValue(cacheResourceLocation.get(cacheKeyFinal));
                return;
            }

            ResourceLocation dynamicResource = TexturePacker.getOrCreateInMemoryTexture(
                    templateTexture,
                    cacheKeyFinal,
                    (baseImg) -> {
                        BufferedImage maskImg = ImageIO.read(mc.getResourceManager().getResource(GLOOM_RL).getInputStream());
                        return TexturePacker.maskTemplateWithPixelSourceByBrightness(
                                maskImg,
                                baseImg,
                                1.0F,
                                15.0F,
                                -1,
                                false
                        );
                    }
            );

            if (dynamicResource != null) {
                cir.setReturnValue(dynamicResource);
            }
        } else if (hasCelestialType(celestialType, CelestialTypeData.CelestialShadow.SPECTRAL.id)) {
            String cacheKeyFinal = new StringBuilder(templateTexture)
                    .append("_")
                    .append(SPECTRAL_RL.getResourcePath())
                    .toString();

            if (cacheResourceLocation.containsKey(cacheKeyFinal)) {
                cir.setReturnValue(cacheResourceLocation.get(cacheKeyFinal));
                return;
            }

            ResourceLocation dynamicResource = TexturePacker.getOrCreateInMemoryTexture(
                    templateTexture,
                    cacheKeyFinal,
                    (baseImg) -> {
                        BufferedImage maskImg = ImageIO.read(mc.getResourceManager().getResource(SPECTRAL_RL).getInputStream());
                        return TexturePacker.maskTemplateWithPixelSourceByBrightness(
                                maskImg,
                                baseImg,
                                1.0F,
                                7.0F,
                                -1,
                                false
                        );
                    }
            );

            if (dynamicResource != null) {
                cir.setReturnValue(dynamicResource);
            }
        }
    }
}
