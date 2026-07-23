package vbonedra.hostiles_are_too_easy.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CelestialTypeCache {
    public static final Map<Integer, Integer> clientCelestialTypeMap = new HashMap<>();
    public static final Set<Integer> requestedEntities = new HashSet<>();

    public static void receiveCelestialTypeFromServer(int entityId, int type) {
        clientCelestialTypeMap.put(entityId, type);
    }
}
