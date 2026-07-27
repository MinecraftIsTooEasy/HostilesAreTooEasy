package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityAICreeperSwell.class)
public abstract class EntityAICreeperSwellMixin {

    @Redirect(method = "updateTask()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/ServerPlayer;canEntityBeSeenFrom(DDDD)Z"))
    private boolean updateTask_explodeThroughWalls(ServerPlayer player, double posX, double posY, double posZ, double maxDistanceSq) {
        return true;
    }
}
