package vbonedra.hostiles_are_too_easy.mixin.achievement;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.AchievementExtend;

import java.util.List;


@Mixin(BlockSkull.class)
public class BlockSkullMixin {

    @Inject(method = "makeWither(Lnet/minecraft/World;IIILnet/minecraft/TileEntitySkull;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;spawnEntityInWorld(Lnet/minecraft/Entity;)Z"))
    private void makeWither_grantAchievement(World world, int x, int y, int z, TileEntitySkull skull, CallbackInfo ci) {
        if (world.isWorldServer()) {
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

    // TODO: that's just duktape over broken bone =( there must be better way to check + what if other mods add custom wither requirements
    @Inject(method = "makeWither(Lnet/minecraft/World;IIILnet/minecraft/TileEntitySkull;)V", at = @At("HEAD"), cancellable = true, remap = false)
    private void makeWither_checkClearArea(World world, int x, int y, int z, TileEntitySkull tileEntity, CallbackInfo ci) {
        if (world.isWorldClient()) {
            return;
        }

        if (tileEntity.getSkullType() == 1 && y >= 2 && world.difficultySetting > 0) {
            int soulSandId = Block.slowSand.blockID;
            boolean structureFound = false;

            for (int var7 = -2; var7 <= 0; ++var7) {
                if (world.getBlockId(x, y - 1, z + var7) == soulSandId
                        && world.getBlockId(x, y - 1, z + var7 + 1) == soulSandId
                        && world.getBlockId(x, y - 2, z + var7 + 1) == soulSandId
                        && world.getBlockId(x, y - 1, z + var7 + 2) == soulSandId
                        && this.isWitherSkull(world, x, y, z + var7)
                        && this.isWitherSkull(world, x, y, z + var7 + 1)
                        && this.isWitherSkull(world, x, y, z + var7 + 2)) {
                    structureFound = true;
                    break;
                }
            }

            if (!structureFound) {
                for (int var10 = -2; var10 <= 0; ++var10) {
                    if (world.getBlockId(x + var10, y - 1, z) == soulSandId
                            && world.getBlockId(x + var10 + 1, y - 1, z) == soulSandId
                            && world.getBlockId(x + var10 + 1, y - 2, z) == soulSandId
                            && world.getBlockId(x + var10 + 2, y - 1, z) == soulSandId
                            && this.isWitherSkull(world, x + var10, y, z)
                            && this.isWitherSkull(world, x + var10 + 1, y, z)
                            && this.isWitherSkull(world, x + var10 + 2, y, z)) {
                        structureFound = true;
                        break;
                    }
                }
            }

            if (structureFound) {
                boolean spaceIsClear = true;

                for (int dy = 1; dy <= 8; dy++) {
                    for (int dx = -8; dx <= 8; dx++) {
                        for (int dz = -8; dz <= 8; dz++) {
                            int currentBlockId = world.getBlockId(x + dx, y + dy, z + dz);

                            if (currentBlockId != 0 && Block.blocksList[currentBlockId] != null && Block.blocksList[currentBlockId].isCollidable()) {
                                spaceIsClear = false;
                                break;
                            }
                        }
                        if (!spaceIsClear) break;
                    }
                    if (!spaceIsClear) break;
                }

                if (!spaceIsClear) {
                    ci.cancel();

                    this.displayActionBarMessage(StatCollector.translateToLocal("notification.wither_block"));
                    this.playClientWitherSound();
                }
            }
        }
    }


    @Unique
    private boolean isWitherSkull(World par1World, int par2, int par3, int par4) {
        if (par1World.getBlockId(par2, par3, par4) != Block.skull.blockID) {
            return false;
        } else {
            TileEntity var6 = par1World.getBlockTileEntity(par2, par3, par4);
            return var6 instanceof TileEntitySkull && ((TileEntitySkull) var6).getSkullType() == 1;
        }
    }
    @Unique
    private void displayActionBarMessage(String text) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.ingameGUI != null) {
            mc.ingameGUI.func_110326_a(text, false);
        }
    }
    @Unique private void playClientWitherSound() {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.sndManager != null) {
            float pitch = (mc.theWorld.rand.nextFloat() - mc.theWorld.rand.nextFloat()) * 0.2F + 1.0F;
            mc.sndManager.playSoundFX("mob.wither.idle", 1.0F, pitch);
        }
    }

}

