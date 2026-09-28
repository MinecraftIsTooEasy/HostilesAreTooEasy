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
import vbonedra.hostiles_are_too_easy.util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.TexturePacker.cacheResourceLocation;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;

@Mixin(RenderShadow.class)
public abstract class RenderShadowMixin {
    @Unique String gloomTexture = "textures/blocks/wool_colored_magenta.png";
    @Unique String spectralTexture = "textures/blocks/wool_colored_yellow.png";

    @Inject(method = "getEntityTexture(Lnet/minecraft/Entity;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getEntityTexture(Entity par1Entity, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (!(par1Entity instanceof EntityShadow shadow)) return;
        if (shadow.worldObj == null) return;
        Minecraft mc = Minecraft.getMinecraft();

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
        if (hasCelestialType(celestialType, CelestialTypeData.CelestialShadow.GLOOM.id)) {
            String cacheKey = templateTexture + "_" + gloomTexture;

            if (!cacheResourceLocation.containsKey(cacheKey)) {
                BufferedImage bufferedImage = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                        gloomTexture,
                        templateTexture,
                        1.0F,
                        15.0F,
                        -1,
                        false
                );

                DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
                ResourceLocation dynamicLocation = mc.getTextureManager().getDynamicTextureLocation(cacheKey, dynamicTexture);
                cacheResourceLocation.put(cacheKey, dynamicLocation);
            }
            cir.setReturnValue(cacheResourceLocation.get(cacheKey));
        } else if (hasCelestialType(celestialType, CelestialTypeData.CelestialShadow.SPECTRAL.id)) {
            String cacheKey = templateTexture + "_" + spectralTexture;

            if (!cacheResourceLocation.containsKey(cacheKey)) {
                BufferedImage bufferedImage = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                        spectralTexture,
                        templateTexture,
                        1.0F,
                        7.0F,
                        -1,
                        false
                );

                DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
                ResourceLocation dynamicLocation = mc.getTextureManager().getDynamicTextureLocation(cacheKey, dynamicTexture);
                cacheResourceLocation.put(cacheKey, dynamicLocation);
            }
            cir.setReturnValue(cacheResourceLocation.get(cacheKey));
        }
    }
}
