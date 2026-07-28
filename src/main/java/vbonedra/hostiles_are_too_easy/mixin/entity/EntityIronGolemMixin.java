package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;
import vbonedra.hostiles_are_too_easy.util.IronGolemBlockType;

@Mixin(EntityIronGolem.class)
public abstract class EntityIronGolemMixin extends EntityGolem implements ICelestialType {
    public EntityIronGolemMixin(World par1World) {
        super(par1World);
    }



    @Shadow private int attackTimer;


    @Inject(method = "applyEntityAttributes()V", at = @At("RETURN"))
    private void applyEntityAttributes(CallbackInfo ci) {
        int celestialType = this.HATE$getCelestialType();
        if (celestialType >= celestialTypeStartPositive) {
            double calculatedHealth = IronGolemBlockType.getMaxHealthForBlockId(celestialType);
            this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setAttribute(calculatedHealth);
            this.setHealth((float)calculatedHealth);
        }
    }

    @Inject(method = "attackEntityAsMob(Lnet/minecraft/Entity;)Lnet/minecraft/EntityDamageResult;", at = @At("HEAD"), cancellable = true)
    private void attackEntityAsMob(Entity target, CallbackInfoReturnable<EntityDamageResult> cir) {
        int celestialType = this.HATE$getCelestialType();
        if (celestialType >= celestialTypeStartPositive) {
            this.attackTimer = 10;
            this.worldObj.setEntityState(this, EnumEntityState.golem_throw);


            DamageSource damageSource = DamageSource.causeMobDamage(this);

            if (IronGolemBlockType.isSilverAspectForBlockId(celestialType)) damageSource.setSilverAspect();
            if (IronGolemBlockType.isMagicAspectForBlockId(celestialType)) damageSource.setMagicAspect();

            Damage damage = new Damage(damageSource, IronGolemBlockType.getBaseDamageForBlockId(celestialType));

            EntityDamageResult result = target.attackEntityFrom(damage);
            if (result == null) {
                cir.setReturnValue(null);
                return;
            }

            if (result.entityWasKnockedBack()) {
                target.motionY += 0.4D;
            }

            this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
            cir.setReturnValue(result);
        }
    }

    @Inject(method = "dropFewItems(ZLnet/minecraft/DamageSource;)V", at = @At("HEAD"), cancellable = true)
    private void dropFewItems(boolean recentlyHitByPlayer, DamageSource damageSource, CallbackInfo ci) {
        int celestialType = this.HATE$getCelestialType();
        if (celestialType >= celestialTypeStartPositive) {
            ci.cancel();

            int numFlowers = this.rand.nextInt(3);
            for (int i = 0; i < numFlowers; ++i) {
                this.dropItem(Block.plantRed.blockID, 1);
            }

            if (recentlyHitByPlayer && !this.has_taken_massive_fall_damage) {
                int numDrops = 3 + this.rand.nextInt(3 + damageSource.getLootingModifier());

                Item matchingNugget = IronGolemBlockType.getItemForBlockId(celestialType);

                if (matchingNugget != null) {
                    for (int i = 0; i < numDrops; ++i) {
                        this.dropItem(matchingNugget.itemID, 1);
                    }
                }
            }
        }
    }


    @Inject(method = "getExperienceValue()I", at = @At("RETURN"), cancellable = true)
    private void getExperienceValue(CallbackInfoReturnable<Integer> cir) {
        int celestialType = this.HATE$getCelestialType();
        if (celestialType >= celestialTypeStartPositive) {
            cir.setReturnValue(cir.getReturnValue() + IronGolemBlockType.getExperienceForBlockId(celestialType));
        }
    }
}
