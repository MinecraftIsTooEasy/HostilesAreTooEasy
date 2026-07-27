package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

import java.util.List;

import static vbonedra.hostiles_are_too_easy.util.DifficultyMode.get_difficulty_level;
import static vbonedra.hostiles_are_too_easy.util.ICelestialType.*;
import static vbonedra.hostiles_are_too_easy.util.RandomUtil.nextIntSafe;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements ICelestialType {
    @Unique private int celestialType = 0;
    @Override public int HATE$getCelestialType() {
        return this.celestialType;
    }
    @Override public void HATE$setCelestialType(int type) {
        this.celestialType = type;
    }
    public EntityLivingBaseMixin(World par1World) {
        super(par1World);
    }

    // NOTE: could be an issue in some cases if super() isnt called in initialization
    @Inject(method = "applyEntityAttributes()V", at = @At("HEAD"))
    private void applyEntityAttributes_celestialTypeInitialization(CallbackInfo ci) {
        World world = this.worldObj;
        if (world != null && !world.isRemote) {
            int difficulty = get_difficulty_level(world);
            if ((Object) this instanceof EntityPhaseSpider) {
                if (this.rand.nextFloat() < (float) difficulty * 0.05F) {
                    this.celestialType = celestialTypeArachnidWarp;
                }
            }
            else if ((Object) this instanceof EntityCreeper) {
                if (this.rand.nextFloat() < difficulty * 0.2F) {
                    this.celestialType = celestialTypeCreeperMimic;
                }
            }
            else if ((Object) this instanceof EntitySkeleton) {
                if (nextIntSafe(world, get_difficulty_level(world) + 1 - (world.isUnderworld() ? 1 : 2)) >= 1) {
                    this.celestialType = celestialTypeSkeletonWithered;
                }
            }
            else if ((Object) this instanceof EntityZombie) {
                if (this.rand.nextFloat() < difficulty * 0.05F) {
                    this.celestialType = celestialTypeZombiePhase;
                }
            }





        }
    }


    @Inject(method = "onDeath(Lnet/minecraft/DamageSource;)V", at = @At("HEAD"))
    private void onDeath_grantBossKillAchievements(DamageSource damageSource, CallbackInfo ci) {
        if ((Object) this instanceof EntityWither wither) {
            if (!wither.worldObj.isRemote) {
                double sqRadius = 16384D;
                List<?> playersNearby = wither.worldObj.playerEntities;
                for (Object obj : playersNearby) {
                    if (obj instanceof EntityPlayer player) {
                        double dX = player.posX - wither.posX;
                        double dY = player.posY - wither.posY;
                        double dZ = player.posZ - wither.posZ;
                        if ((dX * dX + dY * dY + dZ * dZ) <= sqRadius) {
                            if (AchievementExtend.killWither != null) {
                                player.addStat(AchievementExtend.killWither, 1);
                            }
                            if (AchievementExtend.legendaryMode != null) {
                                player.addStat(AchievementExtend.legendaryMode, 1);
                            }
                        }
                    }
                }
            }
        }
        if ((Object) this instanceof EntityDragon dragon) {
            if (!dragon.worldObj.isRemote) {
                double sqRadius = 16384D;
                List<?> playersNearby = dragon.worldObj.playerEntities;
                for (Object obj : playersNearby) {
                    if (obj instanceof EntityPlayer player) {
                        double dX = player.posX - dragon.posX;
                        double dY = player.posY - dragon.posY;
                        double dZ = player.posZ - dragon.posZ;
                        if ((dX * dX + dY * dY + dZ * dZ) <= sqRadius) {
                            if (AchievementExtend.endgameMode != null) {
                                player.addStat(AchievementExtend.endgameMode, 1);
                            }
                        }
                    }
                }
            }
        }
    }

    @Inject(method = "getExperienceValue()I", at = @At("RETURN"), cancellable = true, remap = false)
    private void getExperienceValue(CallbackInfoReturnable<Integer> cir) {
        Object entity = this;

        if (entity instanceof EntitySkeleton) {
            int celestialType = ((ICelestialType) this).HATE$getCelestialType();
            if (celestialType == celestialTypeSkeletonWithered) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityZombie) {
            int celestialType = ((ICelestialType) this).HATE$getCelestialType();
            if (celestialType == celestialTypeZombiePhase) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityCreeper) {
            int celestialType = ((ICelestialType) this).HATE$getCelestialType();
            if (celestialType == celestialTypeCreeperMimic) cir.setReturnValue(cir.getReturnValue() * 2);
        }


    }

    @Inject(method = "getNaturalDefense(Lnet/minecraft/DamageSource;)F", at = @At("RETURN"), cancellable = true, remap = false)
    private void getNaturalDefense(DamageSource damage_source, CallbackInfoReturnable<Float> cir) {
        Object entity = this;

        if (entity instanceof EntitySkeleton) {
            int celestialType = ((ICelestialType) this).HATE$getCelestialType();
            if (celestialType == celestialTypeSkeletonWithered) cir.setReturnValue((cir.getReturnValue() + 1) * 2);
        }
        else if (entity instanceof EntitySilverfish) {
            int celestialType = ((ICelestialType) this).HATE$getCelestialType();
            if (celestialType > 0) {
                Block block = Block.getBlock(celestialType);
                if (block != null) {
                    float hardness = block.getBlockHardness(0);
                    if (hardness < 0.0F) {
                        hardness = 0.0F;
                    }
                    float originalDefense = cir.getReturnValue();
                    cir.setReturnValue(originalDefense + hardness);
                }
            }
        }
    }

    @Inject(method = "writeEntityToNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setInteger("HATECelestialType", this.HATE$getCelestialType());
    }

    @Inject(method = "readEntityFromNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("HATECelestialType")) {
            this.HATE$setCelestialType(par1NBTTagCompound.getInteger("HATECelestialType"));
        }
    }



}
