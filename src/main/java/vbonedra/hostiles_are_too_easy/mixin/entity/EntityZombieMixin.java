package vbonedra.hostiles_are_too_easy.mixin.entity;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

import static vbonedra.hostiles_are_too_easy.util.DifficultyMode.get_difficulty_level;

@Mixin(EntityZombie.class)
public abstract class EntityZombieMixin extends EntityMob implements ICelestialType {
    @Unique private int num_evasions;
    @Unique private int max_num_evasions;
    public EntityZombieMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World world, CallbackInfo ci) {
        int difficulty = get_difficulty_level(this.getWorld());
        if (this.HATE$getCelestialType() == celestialTypeZombiePhase) {
            this.max_num_evasions = this.rand.nextInt(3 + difficulty) + 2;
            this.num_evasions = this.rand.nextInt(this.max_num_evasions);
        }
    }


    @Inject(method = "onUpdate", at = @At("HEAD"))
    public void onUpdate(CallbackInfo ci) {
        if (this.onServer() && this.getHealth() > 0.0F) {
            if (this.num_evasions < this.max_num_evasions && this.getTicksExistedWithOffset() % 200 == 0) {
                ++this.num_evasions;
            }
        }
    }
    @Inject(method = "isAIEnabled", at = @At("RETURN"), cancellable = true)
    protected void isAIEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() == false) {
            cir.setReturnValue(this.HATE$getCelestialType() == celestialTypeZombiePhase);
        }
    }
    @Inject(method = "attackEntityFrom", at = @At("HEAD"), cancellable = true)
    public void attackEntityFrom(Damage damage, CallbackInfoReturnable<EntityDamageResult> cir) {
        if (this.HATE$getCelestialType() == celestialTypeZombiePhase) {
            System.out.println("should evade");
            boolean can_evade = !damage.isFallDamage() && !damage.isFireDamage() && !damage.isPoison();

            if (can_evade && this.num_evasions > 0) {
                System.out.println("can evade " + this.num_evasions);
                --this.num_evasions;
                Entity entity = damage.getImmediateEntity();
                if (entity == null) {
                    entity = damage.getResponsibleEntity();
                }

                if (this.tryTeleportAwayFrom(entity, 3.0F)) {
                    System.out.println("evaded");
                    cir.setReturnValue(null);
                    cir.cancel();
                }
            }
        }
    }

    @Inject(method = "writeEntityToNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void onWriteEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setInteger("HATEPhaseZombie_num_evasions", this.num_evasions);
        par1NBTTagCompound.setInteger("HATEPhaseZombie_max_num_evasions", this.max_num_evasions);
    }

    @Inject(method = "readEntityFromNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void onReadEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("HATEPhaseZombie_num_evasions")) {
            this.num_evasions = par1NBTTagCompound.getInteger("HATEPhaseZombie_num_evasions");
        }
        if (par1NBTTagCompound.hasKey("HATEPhaseZombie_max_num_evasions")) {
            this.max_num_evasions = par1NBTTagCompound.getInteger("HATEPhaseZombie_max_num_evasions");
        }
    }

    // Phase Zombie Logic (maybe move it to util class so it could be used by any other phasing entities?)
    @Unique
    public boolean tryTeleportAwayFrom(Entity entity, double min_distance) {
        if (!this.isDead && !(this.getHealth() <= 0.0F)) {
            double min_distance_sq = min_distance * min_distance;
            int x = this.getBlockPosX();
            int y = this.getFootBlockPosY();
            int z = this.getBlockPosZ();
            double threat_pos_x = entity == null ? this.posX : entity.posX;
            double threat_pos_z = entity == null ? this.posZ : entity.posZ;

            for(int attempts = 0; attempts < 64; ++attempts) {
                int dx = this.rand.nextInt(11) - 5;
                int dy = this.rand.nextInt(9) - 4;
                int dz = this.rand.nextInt(11) - 5;
                if (Math.abs(dx) >= 3 || Math.abs(dz) >= 3) {
                    int try_x = x + dx;
                    int try_y = y + dy;
                    int try_z = z + dz;
                    double try_pos_x = (double)try_x + (double)0.5F;
                    double try_pos_z = (double)try_z + (double)0.5F;
                    if (!(World.getDistanceSqFromDeltas(try_pos_x - threat_pos_x, try_pos_z - threat_pos_z) < min_distance_sq) && try_y >= 1 && this.worldObj.blockExists(try_x, try_y, try_z)) {
                        do {
                            --try_y;
                        } while(!this.worldObj.isBlockSolid(try_x, try_y, try_z) && try_y >= 1);

                        if (try_y >= 1) {
                            ++try_y;
                            if (!this.worldObj.isBlockSolid(try_x, try_y, try_z) && !this.worldObj.isLiquidBlock(try_x, try_y, try_z) && this.tryTeleportTo(try_pos_x, try_y, try_pos_z)) {
                                EntityPlayer target = this.findPlayerToAttack(Math.min(this.getMaxTargettingRange(), 24.0F));
                                if (target != null && target != this.getTarget()) {
                                    this.setTarget(target);
                                }

                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    @Unique
    public boolean tryTeleportTo(double pos_x, double pos_y, double pos_z) {
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
