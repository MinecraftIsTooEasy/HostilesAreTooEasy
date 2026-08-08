package vbonedra.hostiles_are_too_easy.network;

import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;
import vbonedra.hostiles_are_too_easy.util.celestial_type.CelestialSubtypeCache;

public class S2CResponseCelestialSubtype implements Packet {
    private final int entityId;
    private final int celestialSubtype;
    // TODO?: maybe split into 2 packets, one that sends byte and another that sends int, this might improve performance if int for all types is bad
    public S2CResponseCelestialSubtype(int entityId, int celestialSubtype) {
        this.entityId = entityId;
        this.celestialSubtype = celestialSubtype;
    }

    public S2CResponseCelestialSubtype(PacketByteBuf buf) {
        this.entityId = buf.readInt();
        this.celestialSubtype = buf.readShort();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeShort(this.celestialSubtype);
    }

    @Override
    public void apply(EntityPlayer player) {
        if (player != null && player.worldObj != null && player.worldObj.isRemote) {
            CelestialSubtypeCache.receiveCelestialSubtypeFromServer(this.entityId, this.celestialSubtype);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return HATEBackgroundNetwork.RESPONSE_CELESTIAL_SUBTYPE;
    }
}
