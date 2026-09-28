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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeData.hasCelestialType;
import static vbonedra.hostiles_are_too_easy.util.TexturePacker.cacheResourceLocation;

@Mixin(RenderSkeleton.class)
public abstract class RenderSkeletonMixin {
    @Unique String witheredTexture = "textures/entity/skeleton/wither_skeleton.png";

    @Inject(method = "func_110860_a(Lnet/minecraft/EntitySkeleton;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void func_110860_a(EntitySkeleton par1EntitySkeleton, CallbackInfoReturnable<ResourceLocation> cir) throws IOException {
        if (par1EntitySkeleton.worldObj == null) return;
        Minecraft mc = Minecraft.getMinecraft();

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntitySkeleton);

        if (hasCelestialType(celestialType, CelestialTypeData.CelestialSkeleton.WITHERED.id)) {
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = templateTexture + "_" + witheredTexture + "_blendForegroundOverBackgroundWithFactor";

            if (!cacheResourceLocation.containsKey(cacheKey)) {
                BufferedImage bufferedImage = TexturePacker.blendForegroundOverBackgroundWithFactor(
                        ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(templateTexture)).getInputStream()),
                        ImageIO.read(mc.getResourceManager().getResource(new ResourceLocation(witheredTexture)).getInputStream()),
                        2, 1
                );

                DynamicTexture dynamicTexture = new DynamicTexture(bufferedImage);
                ResourceLocation dynamicLocation = mc.getTextureManager().getDynamicTextureLocation(cacheKey, dynamicTexture);
                cacheResourceLocation.put(cacheKey, dynamicLocation);
            }

            cir.setReturnValue(cacheResourceLocation.get(cacheKey));
        }
    }
}
