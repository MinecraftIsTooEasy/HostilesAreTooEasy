package vbonedra.hostiles_are_too_easy.mixin.render_entity;


import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.TexturePacker.cacheResourceLocation;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;


@Mixin(RenderArachnid.class)
public abstract class RenderArachnidMixin {
    @Unique String warpTexture = "textures/items/blocks/wool_colored_lime.png";

    @Inject(method = "getArachnidTextures(Lnet/minecraft/EntityArachnid;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getArachnidTextures(EntityArachnid par1EntityArachnid, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntityArachnid.worldObj == null) return;
        Minecraft mc = Minecraft.getMinecraft();

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityArachnid);

        if (hasCelestialType(celestialType, CelestialTypeData.CelestialArachnid.WARP.id)) {
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = warpTexture + "_" + templateTexture;
            if (!cacheResourceLocation.containsKey(cacheKey)) {
                BufferedImage bufferedImage = TexturePacker.maskTemplateWithPixelSourceByBrightness(
                        warpTexture,
                        templateTexture,
                        3.0F,
                        0.0F,
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
