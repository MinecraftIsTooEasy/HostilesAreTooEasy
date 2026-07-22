package vbonedra.hostiles_are_too_easy.event;

import moddedmite.rustedironcore.api.event.Handlers;
import vbonedra.hostiles_are_too_easy.event.listener.AchievementListener;

import static vbonedra.hostiles_are_too_easy.util.AchievementExtend.registerAchievements;

public class HATEEventRIC extends Handlers {
    public static void register() {
//        FurnaceUpdate.register(new FurnaceListener());
//        Enchanting.register(new EnchantingListener());
//        BiomeGenerate.register(new BiomeGenerateListener());
//        GravelDrop.registerGravelLootEntry(new GravelDropHandler.GravelLootEntry(GravelDropHandler.ObsidianEntry.weight(), info -> Items.nickelNugget.itemID));
//        GravelDrop.registerGravelLootEntry(new GravelDropHandler.GravelLootEntry(GravelDropHandler.MithrilEntry.weight() * 3 / 4, info -> Items.tungstenNugget.itemID));
//        BeaconUpdate.register(new BeaconListener());
//        SpawnCondition.register(new SpawnConditionsRegistry());
//        EntityMobMixin.register(new EntityMobListener());
//        PlayerEvent.register(new PlayerListener());
//        ArrowRegister.register(new IArrowRegisterListener() {
//            @Override
//            public void onRegister(Consumer<Material> registry) {
//                registry.accept(Materials.nickel);
//                registry.accept(Materials.tungsten);
//            }
//        });
//        PropertiesRegistry.register(new PropertyRegistry());
//        Smelting.register(new SmeltingRegistry());
//        Crafting.register(new CraftingRegistry());
//        Trading.register(new TradingListener());
//        Combat.register(new CombatListener());
//        LootTable.register(new LootTableRegistry());
//        EntityTracker.register(new EntityTrackerRegistry());
//        PlayerAttribute.register(new PlayerAttributeListener());
        Achievement.register(new AchievementListener());
        registerAchievements();
//        Barbecue.register(new BarbecueListener());
//        OreGeneration.register(new OreGenerationRegistry());
//        ArmorModel.register(new ArmorModelListener());
//        BiomeDecoration.register(new BiomeDecorationRegistry());
//        PotionRegistry.register(net.oilcake.mitelros.registry.potion.PotionRegistry::register);
//        WorldLoad.register(new WorldLoadListener());
    }

}