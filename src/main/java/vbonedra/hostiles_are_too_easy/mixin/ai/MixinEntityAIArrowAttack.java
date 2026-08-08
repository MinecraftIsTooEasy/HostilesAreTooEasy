package vbonedra.hostiles_are_too_easy.mixin.ai;

import net.minecraft.EntityAIArrowAttack;
import net.minecraft.EntityLiving;
import net.minecraft.EntityLivingBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static vbonedra.hostiles_are_too_easy.util.DifficultyMode.get_difficulty_level;

@Mixin(EntityAIArrowAttack.class)
public class MixinEntityAIArrowAttack {
    @Final @Shadow private EntityLiving entityHost;
    @Shadow private EntityLivingBase attackTarget;

    @Redirect(method = "updateTask", at = @At(value = "FIELD", target = "Lnet/minecraft/EntityAIArrowAttack;maxRangedAttackTime:I"))
    private int updateTask(EntityAIArrowAttack entityAIArrowAttack) {
        int maxRangedAttackTime = entityAIArrowAttack.maxRangedAttackTime;
        if (this.attackTarget != null) {
            double distanceSq = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.posY, this.attackTarget.posZ);
            if (distanceSq < 256.0) {
                double proximity = 1.0 - (Math.sqrt(distanceSq) / 16.0);
                double progressionFactor = 0.5;
                if (this.entityHost.worldObj != null) {
                    int difficulty = get_difficulty_level(this.entityHost.worldObj);
                    progressionFactor = Math.min(1.0, 0.5 + difficulty * 0.25);
                }
                double reductionFactor = 1.0 - (proximity * progressionFactor * 0.9);
                return (int) Math.round((double) maxRangedAttackTime * reductionFactor);
            }
        }
        return maxRangedAttackTime;
    }
}
