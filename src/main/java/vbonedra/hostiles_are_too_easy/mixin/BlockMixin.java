package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;
import vbonedra.hostiles_are_too_easy.util.SilverfishBlockType;

@Mixin(Block.class)
public abstract class BlockMixin {

    @Inject(method = "dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;IIIF)I", at = @At("HEAD"))
    private void dropBlockAsEntityItem_spawnSilverfishCelestialType(BlockBreakInfo info, int id_dropped, int subtype, int quantity, float chance, CallbackInfoReturnable<Integer> cir) {
        if (info != null && info.world != null && !info.world.isRemote) {
            if (info.x == Integer.MAX_VALUE) {
                info.x = info.drop_x;
                info.y = info.drop_y;
                info.z = info.drop_z;
                return;
            }

            if (info.getMetadata() == 1) {
                return;
            }

            Block currentBlock = (Block) (Object) this;
            float spawnChance = SilverfishBlockType.getSpawnChanceForBlockId(currentBlock.blockID);

            if (spawnChance > 0.0F) {
                World world = info.world;
                if (world.rand.nextFloat() <= spawnChance) {
                    EntitySilverfish silverfish = new EntitySilverfish(world);

                    ((ICelestialType) silverfish).HATE$setCelestialType(currentBlock.blockID);

                    silverfish.setLocationAndAngles(
                            (double) info.x + 0.5D,
                            (double) info.y + 0.0D,
                            (double) info.z + 0.5D,
                            world.rand.nextFloat() * 360.0F,
                            0.0F
                    );
                    world.spawnEntityInWorld(silverfish);
                }
            }
        }
    }
}
