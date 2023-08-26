package gg.nuc.advancedprospecting.network;

import gg.nuc.advancedprospecting.blockentities.DebugBlockEntity;
import gg.nuc.advancedprospecting.container.DebugBlockContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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

        CHANNEL.registerMessage(1, DebugBlockTransmutePacket.class, (packet, buffer) -> {
        }, (buffer) -> new DebugBlockTransmutePacket(), (packet, context) -> {
            context.get().enqueueWork(() -> {
                ServerPlayer player = context.get().getSender();
                if (player == null || !(player.containerMenu instanceof DebugBlockContainer menu)) {
                    return;
                }

                BlockPos pos = menu.getPos();
                if (player.level.getBlockEntity(pos) instanceof DebugBlockEntity blockEntity) {
                    blockEntity.transmute();
                    return;
                }
            });
            context.get().setPacketHandled(true);
        });
    }
}
