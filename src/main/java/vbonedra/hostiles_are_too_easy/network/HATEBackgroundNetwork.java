package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;
import moddedmite.rustedironcore.network.PacketReader;

public class HATEBackgroundNetwork {
    public static final ResourceLocation REQUEST_CELESTIAL_TYPE = new ResourceLocation("hostiles_are_too_easy", "request_celestial_type");
    public static final ResourceLocation RESPONSE_CELESTIAL_TYPE = new ResourceLocation("hostiles_are_too_easy", "response_celestial_type");

    public static void init() {
        PacketReader.registerServerPacketReader(REQUEST_CELESTIAL_TYPE, C2SRequestCelestialType::new);

        if (!FishModLoader.isServer()) {
            PacketReader.registerClientPacketReader(RESPONSE_CELESTIAL_TYPE, S2CResponseCelestialType::new);
        }
    }
}
