package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.*;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import vbonedra.hostiles_are_too_easy.util.CelestialTypeCache;

public class S2CResponseCelestialType implements Packet {
    private final int entityId;
    private final int celestialType;
    // TODO?: maybe split into 2 packets, one that sends byte and another that sends int, this might improve performance if int for all types is bad
    public S2CResponseCelestialType(int entityId, int celestialType) {
        this.entityId = entityId;
        this.celestialType = celestialType;
    }

    public S2CResponseCelestialType(PacketByteBuf buf) {
        this.entityId = buf.readInt();
        this.celestialType = buf.readShort();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeShort(this.celestialType);
    }

    @Override
    public void apply(EntityPlayer player) {
        if (player != null && player.worldObj != null && player.worldObj.isRemote) {
            CelestialTypeCache.receiveCelestialTypeFromServer(this.entityId, this.celestialType);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return HATEBackgroundNetwork.RESPONSE_CELESTIAL_TYPE;
    }
}
