package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

import java.util.List;

@Mixin(EntityLivingBase.class)
public class EntityLivingBaseMixin {

    @Inject(method = "onDeath(Lnet/minecraft/DamageSource;)V", at = @At("HEAD"))
    private void grantBossKillAchievements(DamageSource damageSource, CallbackInfo ci) {
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

    @Inject(method = "writeEntityToNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void onWriteEntityToNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (this instanceof ICelestialType) {
            int type = ((ICelestialType) this).HATE$getCelestialType();
            par1NBTTagCompound.setInteger("HATECelestialType", type);
        }
    }

    @Inject(method = "readEntityFromNBT(Lnet/minecraft/NBTTagCompound;)V", at = @At("RETURN"))
    private void onReadEntityFromNBT(NBTTagCompound par1NBTTagCompound, CallbackInfo ci) {
        if (this instanceof ICelestialType) {
            if (par1NBTTagCompound.hasKey("HATECelestialType")) {
                int type = par1NBTTagCompound.getInteger("HATECelestialType");
                ((ICelestialType) this).HATE$setCelestialType(type);
            }
        }
    }
}
