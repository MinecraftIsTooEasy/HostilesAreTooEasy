package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.Block;
import java.util.HashMap;
import java.util.Map;

public class SilverfishBlockType {

    public record BlockData(float spawnChance) {
    }

    public static final Map<Integer, BlockData> blockDataMap = new HashMap<>();

    static {
        blockDataMap.put(Block.blockClay.blockID, new BlockData(1.0F / 4.0F));
        blockDataMap.put(Block.oreCoal.blockID, new BlockData(1.0F / 32.0F));
        blockDataMap.put(Block.oreCopper.blockID, new BlockData(1.0F / 16.0F));
        blockDataMap.put(Block.oreNetherQuartz.blockID, new BlockData(1.0F / 32.0F));
        blockDataMap.put(Block.oreIron.blockID, new BlockData(1.0F / 16.0F));
        blockDataMap.put(Block.oreSilver.blockID, new BlockData(1.0F / 16.0F));
        blockDataMap.put(Block.oreGold.blockID, new BlockData(1.0F));
        blockDataMap.put(Block.oreRedstone.blockID, new BlockData(1.0F / 16.0F));
        blockDataMap.put(Block.oreRedstoneGlowing.blockID, new BlockData(1.0F / 16.0F));
        blockDataMap.put(Block.oreLapis.blockID, new BlockData(1.0F / 16.0F));
        blockDataMap.put(Block.oreDiamond.blockID, new BlockData(1.0F / 4.0F));
        blockDataMap.put(Block.oreEmerald.blockID, new BlockData(1.0F / 4.0F));
        blockDataMap.put(Block.oreMithril.blockID, new BlockData(1.0F / 16.0F));
        blockDataMap.put(Block.oreAdamantium.blockID, new BlockData(1.0F / 8.0F));
    }

    public static float getSpawnChanceForBlockId(int blockId) {
        BlockData data = blockDataMap.get(blockId);
        return data != null ? data.spawnChance : 0.0F;
    }
}
