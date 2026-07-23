package vbonedra.hostiles_are_too_easy.network;

import net.minecraft.*;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.rustedironcore.network.Network;

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
        if (player instanceof ServerPlayer && player.worldObj instanceof WorldServer worldServer) {
            Entity entity = worldServer.getEntityByID(this.entityId);

            NBTTagCompound entityNbt = new NBTTagCompound();
            int celestialType = 0;
            if (entity instanceof EntityCreeper) ((EntityCreeper) entity).writeEntityToNBT(entityNbt);
            if (entity instanceof EntitySkeleton) ((EntitySkeleton) entity).writeEntityToNBT(entityNbt);
            if (entityNbt.hasKey("hate_celestialType")) {
                celestialType = entityNbt.getInteger("hate_celestialType");
            }

            Network.sendToClient((ServerPlayer) player, new S2CResponseCelestialType(this.entityId, celestialType));
        }
    }


    @Override
    public ResourceLocation getChannel() {
        return HATEBackgroundNetwork.REQUEST_CELESTIAL_TYPE;
    }
}
