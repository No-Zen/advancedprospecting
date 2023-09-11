package gg.nuc.advancedprospecting.core.init;

import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.core.network.DebugBlockTransmutePacket;
import gg.nuc.advancedprospecting.core.network.SyncBlockEntityPacket;
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
        AdvancedProspecting.LOGGER.info("Registered {} packets for mod '{}'", index, AdvancedProspecting.MOD_ID);
    }
}
