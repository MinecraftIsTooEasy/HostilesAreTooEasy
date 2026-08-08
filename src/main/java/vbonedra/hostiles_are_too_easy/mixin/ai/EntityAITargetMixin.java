package vbonedra.hostiles_are_too_easy.mixin.ai;

import net.minecraft.EntityAITarget;
import net.minecraft.EntityCreature;
import net.minecraft.Entity;
import net.minecraft.EntityCreeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAITarget.class)
public abstract class EntityAITargetMixin {

    @Shadow protected EntityCreature taskOwner;

    @Inject(method = "shouldCheckSight", at = @At("HEAD"), cancellable = true)
    private void shouldCheckSight(Entity potentialTarget, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            if (this.taskOwner instanceof EntityCreeper) {
                if (!this.taskOwner.worldObj.canBlockSeeTheSky(this.taskOwner.getBlockPosX(), this.taskOwner.getBlockPosY(), this.taskOwner.getBlockPosZ())) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}
