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

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements ICelestialType {

    @Unique private int celestialType = celestialTypeUnset;
    @Override public int HATE$getCelestialType() {
        return this.celestialType;
    }
    @Override public void HATE$setCelestialType(int type) {
        this.celestialType = type;
    }
    @Unique private int celestialSubtype = celestialSubtypeUnset;
    @Override public int HATE$getCelestialSubtype() {
        return this.celestialSubtype;
    }
    @Override public void HATE$setCelestialSubtype(int type) {
        this.celestialSubtype = type;
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
                // TODO: dangerous, caused log spam cause chunk wasn't loaded, might be fixed by chunk check but still requires caution
                boolean isLoaded = world.getChunkProvider().chunkExists(this.getChunkPosX(), this.getChunkPosZ());
                double chanceLurkers = isLoaded && world.canBlockSeeTheSky(this.getBlockPosX(), this.getBlockPosY(), this.getBlockPosZ()) ? 0.05 : world.isOverworld() ? 0.1 : 0.2;
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
                    else if (this.rand.nextFloat() < difficulty * chanceLurkers) {
                        this.celestialType = celestialTypeZombiePlague;
                        this.celestialSubtype = (byte) this.rand.nextInt(256);
                    }
                }
                else if (entity instanceof EntityGhoul) {
                    if (this.rand.nextFloat() < (difficulty + 1) * chanceLurkers) {
                        this.celestialType = celestialTypeGhoulVampire;
                    }
                }
                else if (entity instanceof EntityShadow) {
                    if (this.rand.nextFloat() < (difficulty + 1) * 0.1) {
                        this.celestialType = celestialTypeShadowGloom;
                    }
                    else if (this.rand.nextFloat() < difficulty * 0.1) {
                        this.celestialType = celestialTypeShadowSpectral;
                    }
                }
                else if (entity instanceof EntitySquid) {
                    if (this.rand.nextFloat() < (difficulty + 1) * 0.1) {
                        this.celestialType = celestialTypeSquidGlow;
                    }
                }
                else if (entity instanceof EntityInvisibleStalker) {
                    if (entity.getClass().getSimpleName().equals("EntityInvisibleStalker")) {
                        if (this.rand.nextFloat() < difficulty * 0.05) {
                            this.celestialType = celestialTypeInvisibleStalkerMirror;
                        }
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
                if (celestialType == celestialTypeSkeletonWithered) return value * 2.0D;
            }
            else if (entity instanceof EntitySquid) {
                if (celestialType == celestialTypeSquidGlow) return value * 2.0D;
            }
        }

        return value;
    }


    @Inject(method = "getExperienceValue()I", at = @At("RETURN"), cancellable = true, remap = false)
    private void getExperienceValue(CallbackInfoReturnable<Integer> cir) {
        Object entity = this;
        int celestialType = this.HATE$getCelestialType();

        if (entity instanceof EntitySkeleton) {
            if (celestialType != celestialTypeVanilla) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityZombie) {
            if (celestialType == celestialTypeZombiePhase) cir.setReturnValue(cir.getReturnValue() * 2);
            if (celestialType == celestialTypeZombiePlague) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityCreeper) {
            if (celestialType == celestialTypeCreeperMimic) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityGhoul) {
            if (celestialType == celestialTypeGhoulVampire) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityShadow) {
            if (celestialType == celestialTypeShadowGloom) cir.setReturnValue(cir.getReturnValue() * 2);
            else if (celestialType == celestialTypeShadowSpectral) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntitySquid) {
            if (celestialType == celestialTypeSquidGlow) cir.setReturnValue(cir.getReturnValue() * 2);
        }
        else if (entity instanceof EntityInvisibleStalker) {
            if (celestialType == celestialTypeInvisibleStalkerMirror) cir.setReturnValue(cir.getReturnValue() * 2);
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

    @Inject(method = "attackEntityFrom(Lnet/minecraft/Damage;)Lnet/minecraft/EntityDamageResult;", at = @At("RETURN"))
    private void attackEntityFrom(Damage damage, CallbackInfoReturnable<EntityDamageResult> cir) {
        EntityDamageResult result = cir.getReturnValue();
        if (result != null && result.entityLostHealth() && !result.entityWasDestroyed()) {
            Entity attacker = damage.getResponsibleEntity();
            if (attacker instanceof EntityZombie && attacker instanceof ICelestialType) {
                int celestialType = ((ICelestialType) attacker).HATE$getCelestialType();
                if (celestialType == celestialTypeZombiePlague) {
                    EntityLivingBase victim = (EntityLivingBase) (Object) this;
                    victim.addPotionEffect(new PotionEffect(Potion.poison.id, 480, 0));
                }
            }
        }
    }

    @Inject(method = "writeEntityToNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void writeEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        par1NBTTagCompound.setInteger("HATECelestialType", this.HATE$getCelestialType());
        par1NBTTagCompound.setInteger("HATECelestialSubtype", this.HATE$getCelestialSubtype());
    }

    @Inject(method = "readEntityFromNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void readEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (par1NBTTagCompound.hasKey("HATECelestialType")) {
            this.HATE$setCelestialType(par1NBTTagCompound.getInteger("HATECelestialType"));
            this.HATE$setCelestialSubtype(par1NBTTagCompound.getInteger("HATECelestialSubtype"));
        }
    }


    @Inject(method = "onDeath(Lnet/minecraft/DamageSource;)V", at = @At("HEAD"))
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        if (!this.worldObj.isRemote) {
            if ((Object) this instanceof EntityWither) {
                double sqRadius = 16384D;
                List<?> playersNearby = this.worldObj.playerEntities;
                for (Object obj : playersNearby) {
                    if (obj instanceof EntityPlayer player) {
                        double dX = player.posX - this.posX;
                        double dY = player.posY - this.posY;
                        double dZ = player.posZ - this.posZ;
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
            if ((Object) this instanceof EntityDragon) {
                double sqRadius = 16384D;
                List<?> playersNearby = this.worldObj.playerEntities;
                for (Object obj : playersNearby) {
                    if (obj instanceof EntityPlayer player) {
                        double dX = player.posX - this.posX;
                        double dY = player.posY - this.posY;
                        double dZ = player.posZ - this.posZ;
                        if ((dX * dX + dY * dY + dZ * dZ) <= sqRadius) {
                            if (AchievementExtend.endgameMode != null) {
                                player.addStat(AchievementExtend.endgameMode, 1);
                            }
                        }
                    }
                }
            }
            if ((Object) this instanceof EntityInvisibleStalker) {
                if (this.HATE$getCelestialType() == ICelestialType.celestialTypeInvisibleStalkerMirror) {
                    for (int slot = 0; slot <= 4; slot++) {
                        this.setCurrentItemOrArmor(slot, null);
                    }
                }
            }
        }
    }


}
