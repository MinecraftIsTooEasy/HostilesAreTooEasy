package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.cacheResourceLocation;
import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;

@Mixin(RenderArachnid.class)
public abstract class RenderArachnidMixin {
    @Unique private static final ResourceLocation WARP_RL = new ResourceLocation("textures/items/blocks/wool_colored_lime.png");

    @Inject(method = "getArachnidTextures(Lnet/minecraft/EntityArachnid;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getArachnidTextures(EntityArachnid par1EntityArachnid, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntityArachnid.worldObj == null) return;

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntityArachnid);

        if (hasCelestialType(celestialType, CelestialTypeData.CelestialArachnid.WARP.id)) {
            String templateTexture = cir.getReturnValue().getResourcePath();

            String cacheKeyFinal = new StringBuilder(WARP_RL.getResourcePath())
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
                        BufferedImage maskImg = ImageIO.read(mc.getResourceManager().getResource(WARP_RL).getInputStream());

                        return TexturePacker.maskTemplateWithPixelSourceByBrightness(
                                maskImg,
                                baseImg,
                                3.0F,
                                0.0F,
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
