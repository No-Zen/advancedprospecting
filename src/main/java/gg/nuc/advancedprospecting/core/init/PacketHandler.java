package gg.nuc.advancedprospecting.core.init;

import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.core.network.DebugBlockTransmutePacket;
import gg.nuc.advancedprospecting.core.network.SyncBlockEntityPacket;
import gg.nuc.advancedprospecting.core.network.SyncHeldItemPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(AdvancedProspecting.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void init() {
        int index = 0;
        CHANNEL.messageBuilder(DebugBlockTransmutePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(DebugBlockTransmutePacket::encode).decoder(DebugBlockTransmutePacket::new)
                .consumer(DebugBlockTransmutePacket::handle).add();
        CHANNEL.messageBuilder(SyncBlockEntityPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncBlockEntityPacket::encode).decoder(SyncBlockEntityPacket::new)
                .consumer(SyncBlockEntityPacket::handle).add();
        CHANNEL.messageBuilder(SyncHeldItemPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncHeldItemPacket::encode).decoder(SyncHeldItemPacket::new)
                .consumer(SyncHeldItemPacket::handle).add();
        AdvancedProspecting.LOGGER.info("Registered {} packets for mod '{}'", index, AdvancedProspecting.MOD_ID);
    }

    /*
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
    */
}
