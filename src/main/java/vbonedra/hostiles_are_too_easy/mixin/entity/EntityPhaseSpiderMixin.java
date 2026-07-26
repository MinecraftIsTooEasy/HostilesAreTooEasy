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

@Mixin(EntityPhaseSpider.class)
public abstract class EntityPhaseSpiderMixin extends EntityMob implements ICelestialType {
    @Unique private int celestialType = 0;
    @Override public int HATE$getCelestialType() {
        return this.celestialType;
    }
    @Override public void HATE$setCelestialType(int type) {
        this.celestialType = type;
    }
    public EntityPhaseSpiderMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(World world, CallbackInfo ci) {
        int difficulty = get_difficulty_level(this.getWorld());
        if (this.rand.nextFloat() < difficulty * 0.05F) {
            this.celestialType = celestialTypeArachnidWarp;
        }
    }

    @Inject(method = "tryTeleportTo(DDD)Z", at = @At("RETURN"))
    private void tryTeleportTo(double pos_x, double pos_y, double pos_z, CallbackInfoReturnable<Boolean> cir) {
        if (this.celestialType == celestialTypeArachnidWarp) {
            if (cir.getReturnValue()) {

                double searchRadius = 4.0D;
                AxisAlignedBB searchBox = this.boundingBox.expand(searchRadius, searchRadius, searchRadius);

                IEntitySelector teleportableSelector = entity -> {
                    if (entity instanceof EntityPlayer || entity instanceof EntityArachnid || !entity.isEntityAlive()) return false;
                    return entity instanceof IMob || entity instanceof EntityMob;
                };

                java.util.List<EntityLivingBase> targets = this.worldObj.selectEntitiesWithinAABB(EntityLivingBase.class, searchBox, teleportableSelector);

                EntityLivingBase targetMob = null;
                double closestDistance = Double.MAX_VALUE;

                for (EntityLivingBase entity : targets) {
                    double distance = this.getDistanceSqToEntity(entity);
                    if (distance < closestDistance) {
                        closestDistance = distance;
                        targetMob = entity;
                    }
                }

                if (targetMob != null) {
                    targetMob.setPosition(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ);

                    this.worldObj.blockFX(
                            EnumBlockFX.particle_trail,
                            MathHelper.floor_double(this.lastTickPosX),
                            MathHelper.floor_double(this.lastTickPosY),
                            MathHelper.floor_double(this.lastTickPosZ),
                            (new SignalData())
                                    .setByte(EnumParticle.runegate.ordinal())
                                    .setShort((int)((double)16.0F * World.getDistanceFromDeltas(
                                            this.lastTickPosX - this.posX,
                                            this.lastTickPosY - this.posY,
                                            this.lastTickPosZ - this.posZ
                                    )))
                                    .setApproxPosition(
                                            MathHelper.floor_double(this.posX),
                                            MathHelper.floor_double(this.posY),
                                            MathHelper.floor_double(this.posZ)
                                    )
                    );

                    this.worldObj.playSoundEffect(pos_x, pos_y, pos_z, "mob.endermen.portal", 1.0F, 1.0F);
                    this.worldObj.playSoundEffect(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ, "mob.endermen.portal", 1.0F, 1.0F);

                    if (targetMob instanceof EntityLiving) {
                        ((EntityLiving) targetMob).send_position_update_immediately = true;
                    }
                }
            }
        }
    }


}
