package vbonedra.hostiles_are_too_easy.mixin.ai;

import net.minecraft.EntityAIArrowAttack;
import net.minecraft.EntityLiving;
import net.minecraft.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityAIArrowAttack.class)
public class MixinEntityAIArrowAttack {
    @Final @Shadow private EntityLiving entityHost;
    @Shadow private EntityLivingBase attackTarget;

    @Redirect(method = "updateTask", at = @At(value = "FIELD", target = "Lnet/minecraft/EntityAIArrowAttack;maxRangedAttackTime:I"))
    private int redirectMaxRangedAttackTime(EntityAIArrowAttack entityAIArrowAttack) {
        int maxRangedAttackTime = entityAIArrowAttack.maxRangedAttackTime;
        if (this.attackTarget != null) {
            double distanceSq = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ);
            if (distanceSq < 256.0D) {
                double proximity = 1.0D - (Math.sqrt(distanceSq) / 16.0D);
                double reductionFactor = 1.0D - (proximity * 0.95D);
                return (int) Math.round((double) maxRangedAttackTime * reductionFactor);
            }
        }
        return maxRangedAttackTime;
    }
}
