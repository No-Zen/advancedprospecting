package gg.nuc.advancedprospecting.network;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation("advancedprospecting", "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void register() {
        CHANNEL.registerMessage(0, SyncBlockEntityPacket.class, (packet, buffer) -> {
            buffer.writeBlockPos(packet.getPos());
            buffer.writeNbt(packet.getNbt());
        }, (buffer) -> new SyncBlockEntityPacket(buffer.readBlockPos(), buffer.readNbt()), (packet, context) -> {
            context.get().enqueueWork(() -> {
                Level clientWorld = Minecraft.getInstance().level;
                BlockEntity be = clientWorld.getBlockEntity(packet.getPos());
                if (be != null) {
                    be.load(packet.getNbt());
                }
            });
            context.get().setPacketHandled(true);
        });
    }
}
