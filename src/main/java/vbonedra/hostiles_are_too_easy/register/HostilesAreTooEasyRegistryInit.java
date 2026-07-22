package vbonedra.hostiles_are_too_easy.register;

import net.minecraft.ResourceLocation;
import vbonedra.hostiles_are_too_easy.HostilesAreTooEasyMod;
import huix.glacier.api.entrypoint.IGameRegistry;
import huix.glacier.api.registry.MinecraftRegistry;
import vbonedra.hostiles_are_too_easy.util.TexturePacker;

public class HostilesAreTooEasyRegistryInit implements IGameRegistry {
	// Registrar instance, using this mod's modid as the namespace
	public static final MinecraftRegistry registry = new MinecraftRegistry(HostilesAreTooEasyMod.MOD_ID).initAutoItemRegister();

    // Create an instance of the item
	// public static Item EXAMPLE_ITEM;
    @Override
	public void onGameRegistry() {
		// Register items, bind texture and create localized key
		// registry.registerItem(HostilesAreTooEasyMod.MOD_ID + ":example_item", "exampleItem", EXAMPLE_ITEM);












	}
}
