package vbonedra.hostiles_are_too_easy.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

import static vbonedra.hostiles_are_too_easy.util.RandomUtil.nextIntSafe;

@Mixin(EntitySkeleton.class)
public abstract class EntitySkeletonMixin extends EntityLivingBase implements ICelestialType {
    public EntitySkeletonMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "dropFewItems", at = @At("RETURN"))
    private void dropFewItemsEvolved(boolean recently_hit_by_player, DamageSource damage_source, CallbackInfo ci) {
        if (this.HATE$getCelestialType() == celestialTypeSkeletonWithered) {
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
        if (this.HATE$getCelestialType() == celestialTypeSkeletonWithered) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "isHarmedByLava", at = @At("RETURN"), cancellable = true)
    private void isHarmedByLavaEvolved(CallbackInfoReturnable<Boolean> cir) {
        if (this.HATE$getCelestialType() == celestialTypeSkeletonWithered) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "attackEntityAsMob", at = @At("RETURN"))
    private void attackEntityAsMobEvolved(Entity target, CallbackInfoReturnable<EntityDamageResult> cir) {
        EntityDamageResult result = cir.getReturnValue();
        if (result != null && !result.entityWasDestroyed()) {
            if (result.entityLostHealth() && this.HATE$getCelestialType() == celestialTypeSkeletonWithered && target instanceof EntityLivingBase) {
                target.getAsEntityLivingBase().addPotionEffect(new PotionEffect(Potion.wither.id, 200));
            }
        }
    }
    @Inject(method = "attackEntityWithRangedAttack", at = @At(value = "RETURN"))
    private void attackEntityWithRangedAttackEvolved(EntityLivingBase target, float par2, CallbackInfo ci, @Local EntityArrow var3) {
        if (this.HATE$getCelestialType() == celestialTypeSkeletonWithered) {
            var3.setFire(100);
        }
    }


}
