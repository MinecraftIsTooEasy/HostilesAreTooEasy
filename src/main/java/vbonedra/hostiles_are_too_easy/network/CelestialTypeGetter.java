package vbonedra.hostiles_are_too_easy.network;

import moddedmite.rustedironcore.network.Network;
import net.minecraft.Entity;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialTypeCache;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

public class CelestialTypeGetter {

    public static int getCelestialType(Entity entity) {
        if (entity == null || entity.worldObj == null) {
            return ICelestialType.celestialTypeVanilla;
        }

        int entityId = entity.entityId;
        Integer cachedType = CelestialTypeCache.clientCelestialTypeMap.get(entityId);
        if (cachedType != null) {
            return cachedType;
        }
        if (entity.worldObj.isRemote && !CelestialTypeCache.requestedEntities.contains(entityId)) {
            Network.sendToServer(new C2SRequestCelestialType(entityId));
            CelestialTypeCache.requestedEntities.add(entityId);
        }
        return ICelestialType.celestialTypeVanilla;
    }
}
