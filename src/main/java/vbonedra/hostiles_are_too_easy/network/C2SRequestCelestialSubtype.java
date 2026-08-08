package vbonedra.hostiles_are_too_easy.network;

import moddedmite.rustedironcore.network.Network;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.*;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

public record C2SRequestCelestialSubtype(int entityId) implements Packet {

    public C2SRequestCelestialSubtype(PacketByteBuf buf) {
        this(buf.readInt());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    @Override
    public void apply(EntityPlayer player) {
        if (player instanceof ServerPlayer serverPlayer && player.worldObj instanceof WorldServer worldServer) {
            Entity entity = worldServer.getEntityByID(this.entityId);

            int celestialSubtype = 0;
            if (entity instanceof ICelestialType celestialEntity) {
                celestialSubtype = celestialEntity.HATE$getCelestialSubtype();
            }

            Network.sendToClient(serverPlayer, new S2CResponseCelestialSubtype(this.entityId, celestialSubtype));
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return HATEBackgroundNetwork.REQUEST_CELESTIAL_SUBTYPE;
    }
}
