package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.*;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.rustedironcore.network.Network;
import vbonedra.hostiles_are_too_easy.util.celestial_type.ICelestialType;

public record C2SRequestCelestialType(int entityId) implements Packet {

    public C2SRequestCelestialType(PacketByteBuf buf) {
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

            int celestialType = 0;
            if (entity instanceof ICelestialType celestialEntity) {
                celestialType = celestialEntity.HATE$getCelestialType();
            }

            Network.sendToClient(serverPlayer, new S2CResponseCelestialType(this.entityId, celestialType));
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return HATEBackgroundNetwork.REQUEST_CELESTIAL_TYPE;
    }
}
