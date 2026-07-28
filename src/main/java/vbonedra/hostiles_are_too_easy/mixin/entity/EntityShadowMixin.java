package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

@Mixin(EntityShadow.class)
public abstract class EntityShadowMixin extends EntityMob implements ICelestialType {
    public EntityShadowMixin(World world) {
        super(world);
    }

    @Inject(method = "attackEntityAsMob", at = @At("RETURN"))
    public void attackEntityAsMob(Entity target, CallbackInfoReturnable<EntityDamageResult> cir) {
        EntityDamageResult result = cir.getReturnValue();
        if (result == null) {
            return;
        }

        if (this.HATE$getCelestialType() == ICelestialType.celestialTypeShadowGloom) {
            if (result.entityWasNegativelyAffected() && target instanceof EntityPlayer) {
                EntityPlayer player = target.getAsPlayer();
                player.vision_dimming += target.getAsEntityLivingBase().getAmountAfterResistance(2.0F, 4);
            }

            if (result.entityLostHealth() && target instanceof EntityLivingBase) {
                EntityLivingBase livingTarget = target.getAsEntityLivingBase();

                int lightLevel = this.worldObj.getBlockLightValue(
                        MathHelper.floor_double(this.posX),
                        MathHelper.floor_double(this.posY),
                        MathHelper.floor_double(this.posZ)
                );

                livingTarget.addPotionEffect(new PotionEffect(Potion.wither.id, (int) (((9 - lightLevel) / 9.0F) * 200.0F), 0));


                if (lightLevel < 9) {
                    livingTarget.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
                    if (lightLevel < 4) {
                        livingTarget.addPotionEffect(new PotionEffect(Potion.blindness.id, 100, 0));
                    }
                }
            }
        }
    }

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void onLivingUpdate(CallbackInfo ci) {
        if (!this.worldObj.isRemote) {
            if (this.HATE$getCelestialType() == ICelestialType.celestialTypeShadowGloom) {
                int lightLevel = this.worldObj.getBlockLightValue(
                        MathHelper.floor_double(this.posX),
                        MathHelper.floor_double(this.posY),
                        MathHelper.floor_double(this.posZ)
                );

                float lightFactor = (9 - lightLevel) / 9.0F;

                float speedModifier = 0.23F + (lightFactor * 0.12F);
                float damageModifier = 5.0F + (lightFactor * 5.0F);

                this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setAttribute(speedModifier);
                this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setAttribute(damageModifier);
            }
        }
    }
}
