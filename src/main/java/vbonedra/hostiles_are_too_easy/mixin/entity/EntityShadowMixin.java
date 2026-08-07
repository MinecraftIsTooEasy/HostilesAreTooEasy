package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

@Mixin(EntityShadow.class)
public abstract class EntityShadowMixin extends EntityMob implements ICelestialType {
    public EntityShadowMixin(World world) {
        super(world);
    }


    @Inject(method = "isAIEnabled", at = @At("RETURN"), cancellable = true)
    protected void isAIEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() == false) {
            cir.setReturnValue(this.HATE$getCelestialType() == celestialTypeShadowSpectral);
        }
    }

    @Inject(method = "attackEntityAsMob", at = @At("RETURN"))
    public void attackEntityAsMob(Entity target, CallbackInfoReturnable<EntityDamageResult> cir) {
        EntityDamageResult result = cir.getReturnValue();
        if (result == null) {
            return;
        }

        int celestialType = this.HATE$getCelestialType();
        if (celestialType == ICelestialType.celestialTypeShadowGloom) {
            if (result.entityWasNegativelyAffected() && target instanceof EntityPlayer player) {
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
        if (this.onServer()) {
            int celestialType = this.HATE$getCelestialType();
            if (celestialType == ICelestialType.celestialTypeShadowGloom) {
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
            else if (celestialType == ICelestialType.celestialTypeShadowSpectral) {
                if (this.onServer() && this.getHealth() > 0.0F) {
                    int ticks_existed_with_offset = this.getTicksExistedWithOffset();
                    if ((this.getTarget() != null || this.fleeing) && ticks_existed_with_offset % 10 == 0 && this.rand.nextInt(3) == 0) {
                        PathEntity path = this.worldObj.getPathEntityToEntity(this, this.getTarget(), 32.0F, true, false, this.avoidsPathingThroughWater(), true);
                        if (!path.isFinished()) {
                            int n = path.getNumRemainingPathPoints();
                            if (n > 1) {
                                int path_index_advancement = MathHelper.clamp_int(this.rand.nextInt(n), 1, 4);
                                PathPoint path_point = path.getPathPointFromCurrentIndex(path_index_advancement);
                                System.out.println("0");
                                if ((double)path_point.distanceSqTo(this) > (double)3.0F && this.tryTeleportTo((double)path_point.xCoord + (double)0.5F, path_point.yCoord, (double)path_point.zCoord + (double)0.5F)) {
                                    path.setCurrentPathIndex(path.getCurrentPathIndex() + path_index_advancement - 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    @Unique
    public boolean tryTeleportTo(double pos_x, double pos_y, double pos_z) {
        System.out.println("teleported");
        if (!this.isDead && !(this.getHealth() <= 0.0F)) {
            int x = MathHelper.floor_double(pos_x);
            int y = MathHelper.floor_double(pos_y);
            int z = MathHelper.floor_double(pos_z);
            if (y >= 1 && this.worldObj.blockExists(x, y, z)) {
                while(true) {
                    --y;
                    if (this.worldObj.isBlockSolid(x, y, z)) {
                        ++y;
                        if (!this.worldObj.isBlockSolid(x, y, z) && !this.worldObj.isLiquidBlock(x, y, z)) {
                            double delta_pos_x = pos_x - this.posX;
                            double delta_pos_y = pos_y - this.posY;
                            double delta_pos_z = pos_z - this.posZ;
                            AxisAlignedBB bb = this.boundingBox.translateCopy(delta_pos_x, delta_pos_y, delta_pos_z);
                            if (this.worldObj.getCollidingBoundingBoxes(this, bb).isEmpty() && !this.worldObj.isAnyLiquid(bb)) {
                                double distance = World.getDistanceFromDeltas(delta_pos_x, delta_pos_y, delta_pos_z);
                                this.worldObj.blockFX(EnumBlockFX.particle_trail, x, y, z, (new SignalData()).setByte(EnumParticle.runegate.ordinal()).setShort((int)((double)16.0F * distance)).setApproxPosition(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)));
                                this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, "mob.endermen.portal", 1.0F, 1.0F);
                                this.setPosition(pos_x, pos_y, pos_z);
                                this.send_position_update_immediately = true;
                                return true;
                            }

                            return false;
                        }

                        return false;
                    }

                    if (y < 1) {
                        return false;
                    }

                    --pos_y;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
