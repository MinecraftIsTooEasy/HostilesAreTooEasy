package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.*;
import java.util.HashMap;
import java.util.Map;

public class SilverfishBlockType {
    // TODO: needs playtesting for balancing
    public enum Rarity {
        FREQUENT,
        COMMON,
        UNCOMMON,
        RARE,
        GUARANTEED
    }

    public record BlockData(Rarity rarity, Class<? extends EntitySilverfish> silverfishClass) {
    }

    public static final Map<Integer, BlockData> blockDataMap = new HashMap<>();

    static {
        Class<? extends EntitySilverfish> silverfishClass = EntitySilverfish.class;
        blockDataMap.put(Block.blockClay.blockID, new BlockData(Rarity.FREQUENT, silverfishClass));
        blockDataMap.put(Block.oreSilver.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        blockDataMap.put(Block.oreGold.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        blockDataMap.put(Block.oreLapis.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        silverfishClass = EntityCopperspine.class;
        blockDataMap.put(Block.oreCoal.blockID, new BlockData(Rarity.RARE, silverfishClass));
        blockDataMap.put(Block.oreCopper.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        blockDataMap.put(Block.oreRedstone.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        blockDataMap.put(Block.oreRedstoneGlowing.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        silverfishClass = EntityHoarySilverfish.class;
        blockDataMap.put(Block.oreIron.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        blockDataMap.put(Block.oreDiamond.blockID, new BlockData(Rarity.FREQUENT, silverfishClass));
        blockDataMap.put(Block.oreEmerald.blockID, new BlockData(Rarity.FREQUENT, silverfishClass));
        blockDataMap.put(Block.oreMithril.blockID, new BlockData(Rarity.UNCOMMON, silverfishClass));
        blockDataMap.put(Block.oreAdamantium.blockID, new BlockData(Rarity.COMMON, silverfishClass));
        silverfishClass = EntityNetherspawn.class;
        blockDataMap.put(Block.oreNetherQuartz.blockID, new BlockData(Rarity.RARE, silverfishClass));
    }

    public static float getSpawnChance(int blockId, World world) {
        BlockData data = blockDataMap.get(blockId);
        if (data == null) {
            return 0.0F;
        }

        int difficulty_level = DifficultyMode.get_difficulty_level(world);

        return switch (data.rarity) {
            case FREQUENT -> (1.0F + difficulty_level) / 4.0F;
            case COMMON -> (1.0F + difficulty_level) / 8.0F;
            case UNCOMMON -> (1.0F + difficulty_level) / 16.0F;
            case RARE -> (1.0F + difficulty_level) / 32.0F;
            case GUARANTEED -> 1.0F;
        };
    }

    public static Class<? extends EntitySilverfish> getSilverfishClassForBlockId(int blockId) {
        BlockData data = blockDataMap.get(blockId);
        return data != null ? data.silverfishClass : EntitySilverfish.class;
    }
}
