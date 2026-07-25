package vbonedra.hostiles_are_too_easy.mixin.achievement;

import net.minecraft.BlockSkull;
import net.minecraft.EntityPlayer;
import net.minecraft.TileEntitySkull;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;

import java.util.List;

@Mixin(BlockSkull.class)
public class BlockSkullMixin {

    @Inject(method = "makeWither(Lnet/minecraft/World;IIILnet/minecraft/TileEntitySkull;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;spawnEntityInWorld(Lnet/minecraft/Entity;)Z"))
    private void makeWither_grantAchievement(World world, int x, int y, int z, TileEntitySkull skull, CallbackInfo ci) {
        if (!world.isRemote) {
            double sqRadius = 16384D;

            List<?> playersNearby = world.playerEntities;

            for (Object obj : playersNearby) {
                if (obj instanceof EntityPlayer player) {

                    double dX = player.posX - x;
                    double dY = player.posY - y;
                    double dZ = player.posZ - z;

                    if ((dX * dX + dY * dY + dZ * dZ) <= sqRadius) {
                        player.triggerAchievement(AchievementExtend.spawnWither);
                    }
                }
            }
        }
    }
}
