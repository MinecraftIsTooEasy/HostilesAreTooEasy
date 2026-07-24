package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vbonedra.hostiles_are_too_easy.util.ICelestialType;

@Mixin(BlockClay.class)
public abstract class BlockClayMixin {

    @Inject(method = "dropBlockAsEntityItem(Lnet/minecraft/BlockBreakInfo;)I", at = @At("HEAD"))
    private void onMined(BlockBreakInfo info, CallbackInfoReturnable<Integer> cir) {
        if (info != null && info.world != null && !info.world.isRemote) {

            if (info.x == Integer.MAX_VALUE) {
                info.x = info.drop_x;
                info.y = info.drop_y;
                info.z = info.drop_z;

                return;
            }

            World world = info.world;
            EntitySilverfish silverfish = new EntitySilverfish(world);

            ((ICelestialType) silverfish).HATE$setCelestialType(1);

            silverfish.setLocationAndAngles(
                    (double)info.x + 0.5D,
                    (double)info.y + 0.0D,
                    (double)info.z + 0.5D,
                    world.rand.nextFloat() * 360.0F,
                    0.0F
            );

            world.spawnEntityInWorld(silverfish);
        }
    }

}
