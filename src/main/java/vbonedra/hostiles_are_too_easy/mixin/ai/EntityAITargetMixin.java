package vbonedra.hostiles_are_too_easy.mixin.ai;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAITarget.class)
public abstract class EntityAITargetMixin {

    @Shadow protected EntityCreature taskOwner;

    @Inject(method = "shouldCheckSight", at = @At("RETURN"), cancellable = true)
    private void shouldCheckSight(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            if (this.taskOwner.getClass() == EntityCreeper.class) {
                if (!this.taskOwner.worldObj.canBlockSeeTheSky(this.taskOwner.getBlockPosX(), this.taskOwner.getBlockPosY(), this.taskOwner.getBlockPosZ())) {
                    cir.setReturnValue(false);
                }
            }
            else if (this.taskOwner.isEntityUndead()) {
                if (entity instanceof EntityLivingBase entityLivingBase) {
                    if (entityLivingBase.getHealth() <= entityLivingBase.getMaxHealth() * 0.4) {
                        cir.setReturnValue(false);
                    }
                }
            }
            else if (this.taskOwner.isArthropod()) {
                if (entity instanceof EntityLivingBase entityLivingBase) {
                    if (entityLivingBase.getHealth() <= entityLivingBase.getMaxHealth() * 0.2) {
                        cir.setReturnValue(false);
                    }
                }
            }
        }
    }
}
