package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.*;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;

public class S2CResponseCreeperType implements Packet {
    private int entityId;
    private int creeperType;

    public S2CResponseCreeperType(int entityId, int creeperType) {
        this.entityId = entityId;
        this.creeperType = creeperType;
    }

    public S2CResponseCreeperType(PacketByteBuf buf) {
        this.entityId = buf.readInt();
        this.creeperType = buf.readByte();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeByte(this.creeperType);
    }

    @Override
    public void apply(EntityPlayer player) {
        if (player != null && player.worldObj != null && player.worldObj.isRemote) {
            vbonedra.hostiles_are_too_easy.util.CreeperTypeCache.receiveCreeperTypeFromServer(this.entityId, this.creeperType);
        }
    }


    @Override
    public ResourceLocation getChannel() {
        return HATEBackgroundNetwork.RESPONSE_CREEPER_TYPE;
    }
}
