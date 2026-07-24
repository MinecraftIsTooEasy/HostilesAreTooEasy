package vbonedra.hostiles_are_too_easy.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
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

    @Unique private int celestialType = 0;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World world, CallbackInfo ci) {
        if (nextIntSafe(world, get_difficulty_level(world) + 1 - (world.isUnderworld() ? 1 : 2)) >= 1) {
            this.celestialType = 1;
        }
    }

    @Inject(method = "writeEntityToNBT", at = @At("TAIL"))
    private void writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setInteger("HATECelestialType", this.celestialType);
    }

    @Inject(method = "readEntityFromNBT", at = @At("TAIL"))
    private void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("HATECelestialType")) {
            this.celestialType = par1NBTTagCompound.getInteger("HATECelestialType");
        }
    }

    // logic
    @Inject(method = "dropFewItems", at = @At("RETURN"))
    private void dropFewItemsEvolved(boolean recently_hit_by_player, DamageSource damage_source, CallbackInfo ci) {
        if (this.celestialType == 1) {
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
        if (this.celestialType == 1) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "isHarmedByLava", at = @At("RETURN"), cancellable = true)
    private void isHarmedByLavaEvolved(CallbackInfoReturnable<Boolean> cir) {
        if (this.celestialType == 1) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "attackEntityAsMob", at = @At("RETURN"))
    private void attackEntityAsMobEvolved(Entity target, CallbackInfoReturnable<EntityDamageResult> cir) {
        EntityDamageResult result = cir.getReturnValue();
        if (result != null && !result.entityWasDestroyed()) {
            if (result.entityLostHealth() && this.celestialType == 1 && target instanceof EntityLivingBase) {
                target.getAsEntityLivingBase().addPotionEffect(new PotionEffect(Potion.wither.id, 200));
            }
        }
    }
    @Inject(method = "attackEntityWithRangedAttack", at = @At(value = "RETURN"))
    private void attackEntityWithRangedAttackEvolved(EntityLivingBase target, float par2, CallbackInfo ci, @Local EntityArrow var3) {
        if (this.celestialType == 1) {
            var3.setFire(100);
        }
    }


}
