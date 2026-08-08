package vbonedra.hostiles_are_too_easy.util.celestial_type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CelestialSubtypeCache {
    public static final Map<Integer, Integer> clientCelestialSubtypeMap = new HashMap<>();
    public static final Set<Integer> requestedEntities = new HashSet<>();

    public static void receiveCelestialSubtypeFromServer(int entityId, int type) {
        clientCelestialSubtypeMap.put(entityId, type);
    }
}
