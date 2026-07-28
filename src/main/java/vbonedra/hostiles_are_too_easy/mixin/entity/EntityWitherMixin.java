package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityWither.class)
public abstract class EntityWitherMixin extends EntityMob {
    public EntityWitherMixin(World world) {
        super(world);
    }


    @Shadow protected abstract double func_82214_u(int par1);
    @Shadow protected abstract double func_82208_v(int par1);
    @Shadow protected abstract double func_82213_w(int par1);
    @Shadow public abstract boolean isArmored();


    @Inject(method = "applyEntityAttributes()V", at = @At("RETURN"))
    private void applyEntityAttributes(CallbackInfo ci) {
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(400.0D);
    }

    @ModifyConstant(method = "func_82206_m()V", constant = @Constant(intValue = 220), remap = false)
    private int func_82206_m(int original) {
        return 330;
    }

    @Inject(method = "getExperienceValue", at = @At("RETURN"), cancellable = true)
    public void getExperienceValue(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(cir.getReturnValue() * 10);
    }


    @Inject(method = "func_82209_a(IDDDZ)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void func_82209_a(int headIndex, double targetX, double targetY, double targetZ, boolean isDangerous, CallbackInfo ci) {
        if (this.worldObj.isWorldClient()) {
            return;
        }

        EntityLivingBase target = this.getAttackTarget();


        if (target != null) {
            double dx = target.posX - this.func_82214_u(headIndex);
            double dz = target.posZ - this.func_82213_w(headIndex);
            double distanceSq = dx * dx + dz * dz;

            float lead = (float) Math.pow(distanceSq, 0.44);
            lead *= 0.5F + this.rand.nextFloat();

            targetX = target.getPredictedPosX(lead);
            targetZ = target.getPredictedPosZ(lead);
        }

        double spawnX = this.func_82214_u(headIndex);
        double spawnY = this.func_82208_v(headIndex);
        double spawnZ = this.func_82213_w(headIndex);

        double motionX = targetX - spawnX;
        double motionY = targetY - spawnY;
        double motionZ = targetZ - spawnZ;

        if (this.rand.nextFloat() < 0.025F) {
            this.worldObj.playSoundAtEntity(this, "mob.ghast.scream", 1.0F, 1.0F);

            EntityLargeFireball fireball = new EntityLargeFireball(this.worldObj, this, motionX / 2, motionY / 2, motionZ / 2);
            fireball.field_92057_e = 1;
            fireball.posX = spawnX;
            fireball.posY = spawnY;
            fireball.posZ = spawnZ;

            this.worldObj.spawnEntityInWorld(fireball);
            ci.cancel();
            return;
        }

        this.worldObj.playAuxSFXAtEntity(null, 1014, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

        EntityWitherSkull skull = new EntityWitherSkull(this.worldObj, this, motionX, motionY, motionZ);
        if (isDangerous) {
            skull.setInvulnerable(true);
        }

        skull.posX = spawnX;
        skull.posY = spawnY;
        skull.posZ = spawnZ;

        this.worldObj.spawnEntityInWorld(skull);

        ci.cancel();
    }


    @Inject(
            method = "attackEntityFrom(Lnet/minecraft/Damage;)Lnet/minecraft/EntityDamageResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityMob;attackEntityFrom(Lnet/minecraft/Damage;)Lnet/minecraft/EntityDamageResult;"),
            remap = false
    )
    private void attackEntityFrom(Damage damage, CallbackInfoReturnable<EntityDamageResult> cir) {
        if (!this.worldObj.isRemote && !this.isArmored()) {

            if (this.rand.nextFloat() < 0.10F) {

                this.worldObj.playSoundAtEntity(this, "mob.blaze.breathe", 1.0F, 1.0F);

                int numberOfFireballs = 8;
                double spawnHeight = this.posY + (double)this.getEyeHeight();

                for (int i = 0; i < numberOfFireballs; i++) {
                    double angle = ((double) i * 2.0D * Math.PI) / (double) numberOfFireballs;

                    double motionX = Math.cos(angle);
                    double motionZ = Math.sin(angle);
                    double motionY = (this.rand.nextDouble() - 0.5D) * 0.2D;

                    EntitySmallFireball smallFireball = new EntitySmallFireball(this.worldObj, this, motionX, motionY, motionZ);

                    smallFireball.posX = this.posX + motionX * 1.5D;
                    smallFireball.posY = spawnHeight;
                    smallFireball.posZ = this.posZ + motionZ * 1.5D;

                    this.worldObj.spawnEntityInWorld(smallFireball);
                }
            }
        }
    }
}
