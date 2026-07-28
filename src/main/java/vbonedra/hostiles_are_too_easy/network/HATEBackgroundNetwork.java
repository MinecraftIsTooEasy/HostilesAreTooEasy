package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;
import moddedmite.rustedironcore.network.PacketReader;

import static vbonedra.hostiles_are_too_easy.HostilesAreTooEasyMod.MOD_ID;

public class HATEBackgroundNetwork {
    public static final ResourceLocation REQUEST_CELESTIAL_TYPE = new ResourceLocation("hate:celestialTypeRequest");
    public static final ResourceLocation RESPONSE_CELESTIAL_TYPE = new ResourceLocation("hate:celestialTypeResponse");

    public static void init() {
        PacketReader.registerServerPacketReader(REQUEST_CELESTIAL_TYPE, C2SRequestCelestialType::new);

        if (!FishModLoader.isServer()) {
            PacketReader.registerClientPacketReader(RESPONSE_CELESTIAL_TYPE, S2CResponseCelestialType::new);
        }
    }
}
