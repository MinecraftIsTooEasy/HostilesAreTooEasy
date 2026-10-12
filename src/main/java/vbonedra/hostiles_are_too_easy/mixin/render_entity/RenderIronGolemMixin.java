package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.CelestialSubtypeGetter;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.getBlockTexturePath;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.cacheResourceLocation;

@Mixin(RenderIronGolem.class)
public abstract class RenderIronGolemMixin {

    @Inject(method = "getIronGolemTextures(Lnet/minecraft/EntityIronGolem;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getCustomMetalGolemTextures(EntityIronGolem par1EntityIronGolem, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntityIronGolem.worldObj == null) return;

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityIronGolem);
        int celestialSubtype = CelestialSubtypeGetter.getCelestialSubtype(par1EntityIronGolem);

        if (hasCelestialType(celestialType, CelestialTypeData.CelestialIronGolem.METAL_GOLEM.id)) {
            int metadata = 0;
            int biomeTint = -1;
            Block block = Block.blocksList[celestialSubtype];

            String templateTexture = cir.getReturnValue().getResourcePath();

            String cacheKeyFinal = new StringBuilder()
                    .append(celestialSubtype)
                    .append("_")
                    .append(metadata)
                    .append("_")
                    .append(biomeTint)
                    .append("_")
                    .append(templateTexture)
                    .toString();

            if (cacheResourceLocation.containsKey(cacheKeyFinal)) {
                cir.setReturnValue(cacheResourceLocation.get(cacheKeyFinal));
                return;
            }

            Minecraft mc = Minecraft.getMinecraft();
            ResourceLocation dynamicResource = TexturePacker.getOrCreateInMemoryTexture(
                    templateTexture,
                    cacheKeyFinal,
                    (baseImg) -> {
                        String blockTexPath = getBlockTexturePath(block, metadata, "CelestialIronGolem.METAL_GOLEM");
                        BufferedImage maskImg = ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(blockTexPath)).getInputStream());
                        return TexturePacker.maskTemplateWithPixelSource(
                                maskImg,
                                baseImg,
                                9.0F,
                                1.0f,
                                biomeTint
                        );
                    }
            );

            if (dynamicResource != null) {
                cir.setReturnValue(dynamicResource);
            }
        }
    }
}
