package vbonedra.hostiles_are_too_easy.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CreeperTypeCache {
    public static final Map<Integer, Integer> clientCreeperTypeMap = new HashMap<>();
    public static final Set<Integer> requestedEntities = new HashSet<>();

    public static void receiveCreeperTypeFromServer(int entityId, int type) {
        clientCreeperTypeMap.put(entityId, type);
    }
}
