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

import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.visual_util.TexturePacker.cacheResourceLocation;

@Mixin(RenderSkeleton.class)
public abstract class RenderSkeletonMixin {
    @Unique private static final ResourceLocation WITHERED_RL = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    @Inject(method = "func_110860_a(Lnet/minecraft/EntitySkeleton;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void func_110860_a(EntitySkeleton par1EntitySkeleton, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntitySkeleton.worldObj == null) return;

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntitySkeleton);

        if (hasCelestialType(celestialType, CelestialTypeData.CelestialSkeleton.WITHERED.id)) {
            String templateTexture = cir.getReturnValue().getResourcePath();

            String cacheKeyFinal = new StringBuilder(templateTexture)
                    .append("_")
                    .append(WITHERED_RL.getResourcePath())
                    .append("_blendForegroundOverBackgroundWithFactor")
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
                        BufferedImage foregroundImg = ImageIO.read(mc.getResourceManager().getResource(WITHERED_RL).getInputStream());
                        return TexturePacker.blendForegroundOverBackgroundWithFactor(
                                baseImg,
                                foregroundImg,
                                2,
                                1
                        );
                    }
            );

            if (dynamicResource != null) {
                cir.setReturnValue(dynamicResource);
            }
        }
    }
}
