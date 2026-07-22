package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.World;

import java.util.Random;

public class RandomUtil {
    public static int nextIntSafe(Random rand, int bound) {
        if (bound <= 0) {
            return 0;
        }
        return rand.nextInt(bound);
    }
    public static int nextIntSafe(World world, int bound) {
        if (bound <= 0) {
            return 0;
        }
        return world.rand.nextInt(bound);
    }
}
