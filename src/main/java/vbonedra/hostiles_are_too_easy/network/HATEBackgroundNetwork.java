package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;
import moddedmite.rustedironcore.network.PacketReader;

public class HATEBackgroundNetwork {
    public static final ResourceLocation REQUEST_CREEPER_TYPE = new ResourceLocation("hostiles_are_too_easy", "request_creeper_type");
    public static final ResourceLocation RESPONSE_CREEPER_TYPE = new ResourceLocation("hostiles_are_too_easy", "response_creeper_type");

    public static void init() {
        PacketReader.registerServerPacketReader(REQUEST_CREEPER_TYPE, C2SRequestCreeperType::new);

        if (!FishModLoader.isServer()) {
            PacketReader.registerClientPacketReader(RESPONSE_CREEPER_TYPE, S2CResponseCreeperType::new);
        }
    }
}
