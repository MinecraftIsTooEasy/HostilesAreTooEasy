package vbonedra.hostiles_are_too_easy.mixin.render_entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.network.CelestialTypeGetter;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

import java.util.HashMap;
import java.util.Map;

import static vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType.celestialTypeSkeletonWithered;

@Mixin(RenderSkeleton.class)
public abstract class RenderSkeletonMixin {

    @Unique private final Map<String, ResourceLocation> blendedCache = new HashMap<>();
    @Unique String witheredTexture = "textures/entity/skeleton/wither_skeleton.png";


    @Inject(method = "func_110860_a(Lnet/minecraft/EntitySkeleton;)Lnet/minecraft/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void func_110860_a(EntitySkeleton par1EntitySkeleton, CallbackInfoReturnable<ResourceLocation> cir) {
        if (par1EntitySkeleton.worldObj == null) return;

        int celestialType = CelestialTypeGetter.getCelestialType(par1EntitySkeleton);

        if (celestialType == celestialTypeSkeletonWithered) {
            String templateTexture = cir.getReturnValue().getResourcePath();
            String cacheKey = witheredTexture + "_" + templateTexture;
            if (!blendedCache.containsKey(cacheKey)) {
                ResourceLocation finalBlendedTexture = TexturePacker.blendTextures(
                        witheredTexture,
                        templateTexture,
                        2, 1
                );
                blendedCache.put(cacheKey, finalBlendedTexture);
            }

            cir.setReturnValue(blendedCache.get(cacheKey));
        }
    }
}
