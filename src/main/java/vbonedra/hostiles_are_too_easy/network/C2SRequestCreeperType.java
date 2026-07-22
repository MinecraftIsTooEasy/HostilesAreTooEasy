package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.*;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.rustedironcore.network.Network;

public class C2SRequestCreeperType implements Packet {
    private int entityId;

    public C2SRequestCreeperType(int entityId) {
        this.entityId = entityId;
    }

    public C2SRequestCreeperType(PacketByteBuf buf) {
        this.entityId = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    @Override
    public void apply(EntityPlayer player) {
        if (player instanceof ServerPlayer && player.worldObj instanceof WorldServer) {
            WorldServer worldServer = (WorldServer) player.worldObj;
            Entity entity = worldServer.getEntityByID(this.entityId);

            if (entity instanceof EntityCreeper) {
                NBTTagCompound entityNbt = new NBTTagCompound();
                ((EntityCreeper) entity).writeEntityToNBT(entityNbt);

                int creeperType = 0;
                if (entityNbt.hasKey("hate_CreeperCustomType")) {
                    creeperType = entityNbt.getInteger("hate_CreeperCustomType");
                }

                Network.sendToClient((ServerPlayer) player, new S2CResponseCreeperType(this.entityId, creeperType));
            }
        }
    }


    @Override
    public ResourceLocation getChannel() {
        return HATEBackgroundNetwork.REQUEST_CREEPER_TYPE;
    }
}
