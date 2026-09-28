package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.CelestialSubtypeGetter;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.TexturePacker.getBlockTexturePath;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.TexturePacker.cacheResourceLocation;

@Mixin(RenderSilverfish.class)
public abstract class RenderSilverfishMixin {

    // TODO: move block texture to separate function, its also used in RenderIronGolemMixin.java

    @Inject(method = "getSilverfishTextures(Lnet/minecraft/EntitySilverfish;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getSilverfishTextures(EntitySilverfish par1EntitySilverfish, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntitySilverfish.worldObj == null) return;
        Minecraft mc = Minecraft.getMinecraft();

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntitySilverfish);
        int celestialSubtype = CelestialSubtypeGetter.getCelestialSubtype(par1EntitySilverfish);


        if (hasCelestialType(celestialType, CelestialTypeData.CelestialSilverfish.BLOCKFISH.id)) {
            int metadata = 0;
            int biomeTint = -1;
            Block block = Block.blocksList[celestialSubtype];

            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = celestialSubtype + "_" + metadata + "_" + biomeTint + "_" + templateTexture;

            if (!cacheResourceLocation.containsKey(cacheKey)) {
                BufferedImage bufferedImage = TexturePacker.maskTemplateWithPixelSource(
                        getBlockTexturePath(block, metadata, "CelestialSilverfish.BLOCKFISH"),
                        templateTexture,
                        1.0F,
                        0.0f,
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
