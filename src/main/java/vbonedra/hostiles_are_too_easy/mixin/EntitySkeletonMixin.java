package vbonedra.hostiles_are_too_easy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static vbonedra.hostiles_are_too_easy.difficulty_mode.DifficultyMode.get_difficulty_level;
import static vbonedra.hostiles_are_too_easy.util.RandomUtil.nextIntSafe;

@Mixin(EntitySkeleton.class)
public abstract class EntitySkeletonMixin extends EntityMob {
    public EntitySkeletonMixin(World par1World) {
        super(par1World);
    }


    @Shadow public abstract void setSkeletonType(int par1);


    @Unique int evolved_type = -1;
    @Unique int evolved_from_type = -1;
    @Unique private boolean isSavingNBT = false;

    // spawn
    @Inject(method = "getRandomSkeletonType", at = @At("RETURN"))
    private void getRandomSkeletonType(World world, CallbackInfoReturnable<Integer> cir) {
        this.evolved_from_type = cir.getReturnValue();
        this.evolved_type = cir.getReturnValue();
        if ((this.evolved_type == 0 || this.evolved_type == 1 || this.evolved_type == 2) && nextIntSafe(world, get_difficulty_level(world) + 1 - (world.isUnderworld() ? 1 : 2)) >= 1) {
            this.evolved_type = this.evolved_type == 1 ? 1 : 3;
        }
    }
    @Inject(method = "onSpawnWithEgg", at = @At("RETURN"))
    private void onSpawnWithEgg(EntityLivingData par1EntityLivingData, CallbackInfoReturnable<EntityLivingData> cir) {
        this.setSkeletonType(this.evolved_type);
    }

    @Inject(method = "getSkeletonType", at = @At("RETURN"), cancellable = true)
    public void getSkeletonType(CallbackInfoReturnable<Integer> cir) {
        if (this.worldObj != null && this.worldObj.isRemote) return;
        if (this.isSavingNBT) return;

        if (this.evolved_type == 3) {
            cir.setReturnValue(this.evolved_from_type);
        }
    }

    // NBT
    @Inject(method = "writeEntityToNBT", at = @At("HEAD"))
    private void writeEntityToNBTHead(NBTTagCompound tagCompound, CallbackInfo ci) {
        this.isSavingNBT = true;
    }
    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void writeEntityToNBTTail(NBTTagCompound tagCompound, CallbackInfo ci) {
        tagCompound.setInteger("EvolvedType", this.evolved_type);
        tagCompound.setInteger("EvolvedFromType", this.evolved_from_type);
        this.isSavingNBT = false;
    }
    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void readEntityFromNBT(NBTTagCompound tagCompound, CallbackInfo ci) {
        if (tagCompound.hasKey("EvolvedType")) this.evolved_type = tagCompound.getInteger("EvolvedType");
        if (tagCompound.hasKey("EvolvedFromType")) this.evolved_from_type = tagCompound.getInteger("EvolvedFromType");
    }

    // logic
    @Inject(method = "dropFewItems", at = @At("RETURN"))
    private void dropFewItemsEvolved(boolean recently_hit_by_player, DamageSource damage_source, CallbackInfo ci) {
        if (this.evolved_type == 3) {
            int looting = damage_source.getLootingModifier();
            int num_drops = this.rand.nextInt(3 + looting) - 1;
            if (num_drops > 0 && !recently_hit_by_player) {
                num_drops -= this.rand.nextInt(num_drops + 1);
            }

            for(int i = 0; i < num_drops; ++i) {
                this.dropItem(Item.coal.itemID, 1);
            }

            if (recently_hit_by_player && !this.has_taken_massive_fall_damage && this.rand.nextInt(this.getBaseChanceOfRareDrop()) < 5 + looting * 2 && this.rand.nextFloat() <= 0.25) {
                this.dropItemStack(new ItemStack(Item.skull.itemID, 1, 1), 0.0F);
            }

        }
    }
    @Inject(method = "isHarmedByFire", at = @At("RETURN"), cancellable = true)
    private void isHarmedByFireEvolved(CallbackInfoReturnable<Boolean> cir) {
        if (this.evolved_type == 3) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "isHarmedByLava", at = @At("RETURN"), cancellable = true)
    private void isHarmedByLavaEvolved(CallbackInfoReturnable<Boolean> cir) {
        if (this.evolved_type == 3) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "attackEntityAsMob", at = @At("RETURN"))
    private void attackEntityAsMobEvolved(Entity target, CallbackInfoReturnable<EntityDamageResult> cir) {
        EntityDamageResult result = cir.getReturnValue();
        if (result != null && !result.entityWasDestroyed()) {
            if (result.entityLostHealth() && this.evolved_type == 3 && target instanceof EntityLivingBase) {
                target.getAsEntityLivingBase().addPotionEffect(new PotionEffect(Potion.wither.id, 200));
            }
        }
    }
    @Inject(method = "attackEntityWithRangedAttack", at = @At(value = "RETURN"))
    private void attackEntityWithRangedAttackEvolved(EntityLivingBase target, float par2, CallbackInfo ci, @Local EntityArrow var3) {
        if (this.evolved_type == 3) {
            var3.setFire(100);
        }
    }


}
