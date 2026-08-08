package vbonedra.hostiles_are_too_easy.network;

import moddedmite.rustedironcore.network.Network;
import net.minecraft.Entity;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialSubtypeCache;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

public class CelestialSubtypeGetter {

    public static int getCelestialSubtype(Entity entity) {
        if (entity == null || entity.worldObj == null) {
            return ICelestialType.celestialSubtypeVanilla;
        }

        int entityId = entity.entityId;
        Integer cachedSubtype = CelestialSubtypeCache.clientCelestialSubtypeMap.get(entityId);
        if (cachedSubtype != null) {
            return cachedSubtype;
        }
        if (entity.worldObj.isRemote && !CelestialSubtypeCache.requestedEntities.contains(entityId)) {
            Network.sendToServer(new C2SRequestCelestialSubtype(entityId));
            CelestialSubtypeCache.requestedEntities.add(entityId);
        }
        return ICelestialType.celestialSubtypeVanilla;
    }
}
