package vbonedra.hostiles_are_too_easy.mixin;

import net.minecraft.WorldClient;
import net.minecraft.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeCache;

@Mixin(WorldClient.class)
public class MixinWorldClient {

    @Inject(method = "onEntityRemoved(Lnet/minecraft/Entity;)V", at = @At("HEAD"))
    private void hostilesAreTooEasy$clearCelestialCacheOnRemove(Entity par1Entity, CallbackInfo ci) {
        if (par1Entity != null) {
            int entityId = par1Entity.entityId;
            CelestialTypeCache.clientCelestialTypeMap.remove(entityId);
            CelestialTypeCache.requestedEntities.remove(entityId);
        }
    }
}
