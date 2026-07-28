package vbonedra.hostiles_are_too_easy.util;

import net.minecraft.*;
import java.util.HashMap;
import java.util.Map;

public class IronGolemBlockType {

    public record GolemBlockData(
            EnumEquipmentMaterial material,
            Item matchingNugget,
            boolean isSilver,
            boolean isMagic,
            float regenPercentage
    ) {
        public GolemBlockData(EnumEquipmentMaterial material, Item matchingNugget, boolean isSilver, boolean isMagic) {
            this(material, matchingNugget, isSilver, isMagic, 0.05F);
        }

        public GolemBlockData(EnumEquipmentMaterial material, Item matchingNugget) {
            this(material, matchingNugget, false, false);
        }

        public GolemBlockData setSilverAspect() {
            return new GolemBlockData(this.material, this.matchingNugget, true, this.isMagic, this.regenPercentage);
        }

        public GolemBlockData setMagicAspect() {
            return new GolemBlockData(this.material, this.matchingNugget, this.isSilver, true, this.regenPercentage);
        }

        public GolemBlockData setCustomRegen(float multiplier) {
            return new GolemBlockData(this.material, this.matchingNugget, this.isSilver, this.isMagic, this.regenPercentage * multiplier);
        }
    }

    public static final Map<Integer, GolemBlockData> golemDataMap = new HashMap<>();

    private static final GolemBlockData DEFAULT_DATA = new GolemBlockData(EnumEquipmentMaterial.iron, Item.ironNugget);

    static {
        golemDataMap.put(Block.blockCopper.blockID, new GolemBlockData(EnumEquipmentMaterial.copper, Item.copperNugget).setCustomRegen(0.5F));
        golemDataMap.put(Block.blockSilver.blockID, new GolemBlockData(EnumEquipmentMaterial.silver, Item.silverNugget).setSilverAspect());

        golemDataMap.put(Block.blockGold.blockID, new GolemBlockData(EnumEquipmentMaterial.gold, Item.goldNugget).setMagicAspect().setCustomRegen(3.0F));

        golemDataMap.put(Block.blockIron.blockID, DEFAULT_DATA);
        golemDataMap.put(Block.blockAncientMetal.blockID, new GolemBlockData(EnumEquipmentMaterial.ancient_metal, Item.ancientMetalNugget));

        golemDataMap.put(Block.blockMithril.blockID, new GolemBlockData(EnumEquipmentMaterial.mithril, Item.mithrilNugget).setMagicAspect().setCustomRegen(2.0F));

        golemDataMap.put(Block.blockAdamantium.blockID, new GolemBlockData(EnumEquipmentMaterial.adamantium, Item.adamantiumNugget).setCustomRegen(0.5F));

        golemDataMap.put(Block.blockNetherQuartz.blockID, new GolemBlockData(EnumEquipmentMaterial.quartz, Item.shardNetherQuartz).setMagicAspect().setCustomRegen(1.5F));
        golemDataMap.put(Block.blockEmerald.blockID, new GolemBlockData(EnumEquipmentMaterial.emerald, Item.shardEmerald).setMagicAspect());
        golemDataMap.put(Block.blockDiamond.blockID, new GolemBlockData(EnumEquipmentMaterial.diamond, Item.shardDiamond).setMagicAspect());
    }


    public static GolemBlockData getGolemData(int blockId) {
        GolemBlockData data = golemDataMap.get(blockId);
        return data != null ? data : DEFAULT_DATA;
    }

    public static float getDurabilityForBlockId(int blockId) {
        return getMaterialForBlockId(blockId).durability;
    }

    public static float getMaxHealthForBlockId(int blockId) {
        return (float) (24 * Math.sqrt(getDurabilityForBlockId(blockId)));
    }

    public static float getNaturalDefenseForBlockId(int blockId) {
        return (float) Math.cbrt(getDurabilityForBlockId(blockId));
    }

    public static float getBaseDamageForBlockId(int blockId) {
        float durability = getDurabilityForBlockId(blockId);
        return (float) (4F * Math.cbrt(durability));
    }

    public static int getExperienceForBlockId(int blockId) {
        float durability = getDurabilityForBlockId(blockId);
        return (int) Math.sqrt((durability * durability * durability));
    }

    public static Item getItemForBlockId(int blockId) {
        return getGolemData(blockId).matchingNugget;
    }

    public static EnumEquipmentMaterial getMaterialForBlockId(int blockId) {
        return getGolemData(blockId).material;
    }

    public static boolean isValidGolemBlock(int blockId) {
        return golemDataMap.containsKey(blockId);
    }

    public static boolean isSilverAspectForBlockId(int blockId) {
        return getGolemData(blockId).isSilver;
    }

    public static boolean isMagicAspectForBlockId(int blockId) {
        return getGolemData(blockId).isMagic;
    }

    public static float getRegenPercentageForBlockId(int blockId) {
        return getGolemData(blockId).regenPercentage;
    }
}
