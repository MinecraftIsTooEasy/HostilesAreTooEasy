package vbonedra.hostiles_are_too_easy;

import vbonedra.hostiles_are_too_easy.event.HATEEventRIC;
import net.fabricmc.api.ModInitializer;

import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vbonedra.hostiles_are_too_easy.network.HATEBackgroundNetwork;

public class HostilesAreTooEasyMod implements ModInitializer {
    public static final String MOD_ID = "hostiles_are_too_easy";
    public static final String MOD_NAME = "HostilesAreTooEasy";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger HATE_LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        HATE_LOGGER.info("Zombies are Too Easy! Creepers are Too Easy! Phase Spiders are Too Easy! Too Easy! Too Easy! Too Easy!");
        HATEBackgroundNetwork.init();
        // Add resource pack domain, default "minecraft"
        ModResourceManager.addResourcePackDomain(MOD_ID);

        //Register an event listening object
        HATEEventRIC.register();
    }
}
