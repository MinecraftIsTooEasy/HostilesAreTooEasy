package vbonedra.hostiles_are_too_easy;

import vbonedra.hostiles_are_too_easy.event.HATEEventRIC;
import net.fabricmc.api.ModInitializer;

import net.xiaoyu233.fml.ModResourceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import vbonedra.hostiles_are_too_easy.network.HATEBackgroundNetwork;

public class HostilesAreTooEasyMod implements ModInitializer {
    public static final String MOD_ID = "hostiles_are_too_easy";
    public static final String MOD_ID_COMPACT = "hate";
    public static final String MOD_NAME = "HostilesAreTooEasy";

    public static final Logger HATE_LOGGER = LogManager.getLogger(MOD_NAME);

    @Override
    public void onInitialize() {
        HATE_LOGGER.info("Zombies are Too Easy! Creepers are Too Easy! Phase Spiders are Too Easy! Too Easy! Too Easy! Too Easy!");
        HATEBackgroundNetwork.init();
        ModResourceManager.addResourcePackDomain(MOD_ID);

        HATEEventRIC.register();
    }
}
