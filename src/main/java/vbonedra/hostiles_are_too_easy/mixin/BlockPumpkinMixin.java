package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;
import vbonedra.hostiles_are_too_easy.util.IronGolemBlockType;

@Mixin(BlockPumpkin.class)
public abstract class BlockPumpkinMixin {

    @Inject(method = "onBlockAdded(Lnet/minecraft/World;III)V", at = @At("HEAD"), cancellable = true)
    private void HATE$checkForCustomMetalGolems(World world, int x, int y, int z, CallbackInfo ci) {
//        if (world.isWorldClient()) {
//            return;
//        }

        int blockId = world.getBlockId(x, y - 1, z);

        if (blockId == Block.blockIron.blockID || blockId == 0) {
            return;
        }

        if (!IronGolemBlockType.isValidGolemBlock(blockId)) {
            return;
        }

        if (world.getBlockId(x, y - 2, z) != blockId) {
            return;
        }

        boolean armX = world.getBlockId(x - 1, y - 1, z) == blockId && world.getBlockId(x + 1, y - 1, z) == blockId;
        boolean armZ = world.getBlockId(x, y - 1, z - 1) == blockId && world.getBlockId(x, y - 1, z + 1) == blockId;

        if (armX || armZ) {
            ci.cancel();

            world.setBlock(x, y, z, 0, 0, 2);
            world.setBlock(x, y - 1, z, 0, 0, 2);
            world.setBlock(x, y - 2, z, 0, 0, 2);

            if (armX) {
                world.setBlock(x - 1, y - 1, z, 0, 0, 2);
                world.setBlock(x + 1, y - 1, z, 0, 0, 2);
            } else {
                world.setBlock(x, y - 1, z - 1, 0, 0, 2);
                world.setBlock(x, y - 1, z + 1, 0, 0, 2);
            }

            EntityIronGolem customGolem = new EntityIronGolem(world);
            customGolem.setPlayerCreated(true);
            customGolem.setLocationAndAngles((double)x + 0.5D, (double)y - 1.95D, (double)z + 0.5D, 0.0F, 0.0F);

            ((ICelestialType) customGolem).HATE$setCelestialType(blockId);
            customGolem.reapplyEntityAttributes();

            world.spawnEntityInWorld(customGolem);

            for (int i = 0; i < 120; ++i) {
                world.spawnParticle(EnumParticle.snowballpoof, (double)x + world.rand.nextDouble(), (double)(y - 2) + world.rand.nextDouble() * 3.9D, (double)z + world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
            }

            world.notifyBlockChange(x, y, z, 0);
            world.notifyBlockChange(x, y - 1, z, 0);
            world.notifyBlockChange(x, y - 2, z, 0);

            if (armX) {
                world.notifyBlockChange(x - 1, y - 1, z, 0);
                world.notifyBlockChange(x + 1, y - 1, z, 0);
            } else {
                world.notifyBlockChange(x, y - 1, z - 1, 0);
                world.notifyBlockChange(x, y - 1, z + 1, 0);
            }
        }
    }
}
