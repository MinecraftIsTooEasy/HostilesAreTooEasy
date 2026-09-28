package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.CelestialSubtypeGetter;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.TexturePacker.getBlockTexturePath;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.TexturePacker.cacheResourceLocation;

@Mixin(RenderIronGolem.class)
public abstract class RenderIronGolemMixin {

    @Inject(method = "getIronGolemTextures(Lnet/minecraft/EntityIronGolem;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getCustomMetalGolemTextures(EntityIronGolem par1EntityIronGolem, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntityIronGolem.worldObj == null) return;
        Minecraft mc = Minecraft.getMinecraft();

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityIronGolem);
        int celestialSubtype = CelestialSubtypeGetter.getCelestialSubtype(par1EntityIronGolem);


        if (hasCelestialType(celestialType, CelestialTypeData.CelestialIronGolem.METAL_GOLEM.id)) {
            int metadata = 0;
            int biomeTint = -1;
            Block block = Block.blocksList[celestialSubtype];

            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = celestialSubtype + "_" + metadata + "_" + biomeTint + "_" + templateTexture;

            if (!cacheResourceLocation.containsKey(cacheKey)) {
                BufferedImage bufferedImage = TexturePacker.maskTemplateWithPixelSource(
                        getBlockTexturePath(block, metadata, "CelestialIronGolem.METAL_GOLEM"),
                        templateTexture,
                        4.0F,
                        1.0f,
                        biomeTint
                );

                DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
                ResourceLocation dynamicLocation = mc.getTextureManager().getDynamicTextureLocation(cacheKey, dynamicTexture);
                cacheResourceLocation.put(cacheKey, dynamicLocation);
            }

            cir.setReturnValue(cacheResourceLocation.get(cacheKey));
        }
    }
}
