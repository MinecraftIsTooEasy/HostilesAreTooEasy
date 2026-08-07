package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;
import vbonedra.hostiles_are_too_easy.util.celestial_type.IronGolemBlockType;

import java.util.List;

import static vbonedra.hostiles_are_too_easy.util.DifficultyMode.get_difficulty_level;
import static vbonedra.hostiles_are_too_easy.util.RandomUtil.nextIntSafe;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements ICelestialType {
    @Unique private int celestialType = celestialTypeUnset;
    @Override public int HATE$getCelestialType() {
        return this.celestialType;
    }
    @Override public void HATE$setCelestialType(int type) {
        this.celestialType = type;
    }
    public EntityLivingBaseMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "applyEntityAttributes()V", at = @At("HEAD"))
    private void applyEntityAttributes_celestialTypeInitialization(CallbackInfo ci) {
        if (this.celestialType == celestialTypeUnset) {
            // TODO: add canXray data
            this.celestialType = celestialTypeVanilla;
            World world = this.worldObj;
            Object entity = this;
            if (world != null && world.isWorldServer()) {
                int difficulty = get_difficulty_level(world);
                double chanceLurkers = world.canBlockSeeTheSky(this.getBlockPosX(), this.getBlockPosY(), this.getBlockPosZ()) ? 0.05 : world.isOverworld() ? 0.1 : 0.2;
                if (entity instanceof EntityPhaseSpider) {
                    if (this.rand.nextFloat() < difficulty * chanceLurkers * 0.5) {
                        this.celestialType = celestialTypePhaseSpiderWarp;
                    }
                }
                else if (entity instanceof EntityCreeper) {
                    if (this.rand.nextFloat() < difficulty * 0.2) {
                        this.celestialType = celestialTypeCreeperMimic;
                    }

                }
                else if (entity instanceof EntitySkeleton) {
                    if (this.rand.nextFloat() < (difficulty - (world.isOverworld() ? 1 : 0)) * chanceLurkers) {
                        this.celestialType = celestialTypeSkeletonWithered;
                    }
                }
                else if (entity instanceof EntityZombie) {
                    if (this.rand.nextFloat() < (difficulty + 1) * chanceLurkers * 0.5) {
                        this.celestialType = celestialTypeZombiePhase;
                    }
                }
                else if (entity instanceof EntityGhoul) {
                    if (this.rand.nextFloat() < (difficulty + 1) * chanceLurkers) {
                        this.celestialType = celestialTypeGhoulVampire;
                    }
                }
                else if (entity instanceof EntityShadow) {
                    if (this.rand.nextFloat() < difficulty * 0.1) {
                        this.celestialType = celestialTypeShadowGloom;
                    }
                }

            }
        }
    }

    @ModifyVariable(method = "setEntityAttribute(Lnet/minecraft/Attribute;D)Lnet/minecraft/AttributeInstance;", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private double setEntityAttribute(double value, Attribute attribute) {
        Object entity = this;
        int celestialType = this.HATE$getCelestialType();

        if (attribute == SharedMonsterAttributes.maxHealth) {
            if (entity instanceof EntitySkeleton) {
                if (celestialType == celestialTypeSkeletonWithered) {
                    return value * 2.0D;
                }
            }
        }

        return value;
    }


    @Inject(method = "getExperienceValue()I", at = @At("RETURN"), cancellable = true, remap = false)
    private void getExperienceValue(CallbackInfoReturnable<Integer> cir) {
        Object entity = this;
        int celestialType = this.HATE$getCelestialType();

        if (entity instanceof EntitySkeleton) {
            if (celestialType == celestialTypeSkeletonWithered) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityZombie) {
            if (celestialType == celestialTypeZombiePhase) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityCreeper) {
            if (celestialType == celestialTypeCreeperMimic) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityGhoul) {
            if (celestialType == celestialTypeGhoulVampire) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityShadow) {
            if (celestialType == celestialTypeShadowGloom) cir.setReturnValue(cir.getReturnValue() * 2);
        }

    }

    @Inject(method = "getNaturalDefense(Lnet/minecraft/DamageSource;)F", at = @At("RETURN"), cancellable = true, remap = false)
    private void getNaturalDefense(DamageSource damage_source, CallbackInfoReturnable<Float> cir) {
        Object entity = this;
        int celestialType = this.HATE$getCelestialType();

        if (entity instanceof EntityIronGolem) {
            if (celestialType >= 0) {
                if (damage_source.bypassesMundaneArmor()) {
                    cir.setReturnValue(cir.getReturnValue() + IronGolemBlockType.getNaturalDefenseForBlockId(celestialType));
                }
            }
        }
        if (entity instanceof EntityGhoul) {
            if (celestialType == celestialTypeGhoulVampire) cir.setReturnValue(cir.getReturnValue() + 4);
        }
        else if (entity instanceof EntitySilverfish) {
            if (celestialType > 0) {
                Block block = Block.getBlock(celestialType);
                if (block != null) {
                    cir.setReturnValue(cir.getReturnValue() + Math.min(0f, block.getBlockHardness(0)));
                }
            }
        }
    }

    @ModifyConstant(method = "onLivingUpdate()V", constant = @Constant(floatValue = 0.1F))
    private float modifyRegenPercentage(float original) {
        Object entity = this;
        int celestialType = this.HATE$getCelestialType();

        if (entity instanceof EntityIronGolem) {
            if (celestialType >= 0) {
                if (IronGolemBlockType.isValidGolemBlock(celestialType)) {
                    return IronGolemBlockType.getRegenPercentageForBlockId(celestialType);
                }
            }
        }

        return original;
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


}
