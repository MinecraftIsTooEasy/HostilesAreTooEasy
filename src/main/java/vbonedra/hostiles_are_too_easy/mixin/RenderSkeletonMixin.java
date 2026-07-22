package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.RenderSkeleton;
import net.minecraft.EntitySkeleton;
import net.minecraft.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

@Mixin(RenderSkeleton.class)
public abstract class RenderSkeletonMixin {

    @Unique
    private static final ResourceLocation[] texturesWithered = new ResourceLocation[5];

    @Inject(method = "setTextures", at = @At("RETURN"))
    private void initializeWitheredTextures(CallbackInfo ci) {
        texturesWithered[0] = TexturePacker.blendTextures(
                "textures/entity/skeleton/wither_skeleton.png",
                "textures/entity/skeleton/skeleton.png",
                2, 1
        );
        texturesWithered[1] = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");
        texturesWithered[2] = TexturePacker.blendTextures(
                "textures/entity/skeleton/wither_skeleton.png",
                "textures/entity/skeleton/longdead.png",
                2, 1
        );
        texturesWithered[3] = TexturePacker.blendTextures(
                "textures/entity/skeleton/wither_skeleton.png",
                "textures/entity/skeleton/longdead_guardian.png",
                2, 1
        );
        texturesWithered[4] = TexturePacker.blendTextures(
                "textures/entity/skeleton/wither_skeleton.png",
                "textures/entity/skeleton/bone_lord.png",
                2, 1
        );
    }

    @Inject(method = "func_110860_a", at = @At("RETURN"), cancellable = true)
    private void applyWitherTextureToAllEvolved(EntitySkeleton par1EntitySkeleton, CallbackInfoReturnable<ResourceLocation> cir) {
        if (par1EntitySkeleton.getSkeletonType() == 3) {
            if (par1EntitySkeleton.isLongdead()) {
                cir.setReturnValue(texturesWithered[par1EntitySkeleton.isLongdeadGuardian() ? 3 : 2]);
            } else if (par1EntitySkeleton.isBoneLord()) {
                cir.setReturnValue(texturesWithered[par1EntitySkeleton.isAncientBoneLord() ? 3 : 4]);
            } else {
                cir.setReturnValue(texturesWithered[0]);
            }
        }
    }
}
