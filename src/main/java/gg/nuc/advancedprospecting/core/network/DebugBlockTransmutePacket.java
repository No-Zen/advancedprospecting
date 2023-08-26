package gg.nuc.advancedprospecting.core.network;

import gg.nuc.advancedprospecting.common.block.entity.DebugBlockEntity;
import gg.nuc.advancedprospecting.common.container.DebugBlockContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DebugBlockTransmutePacket {

    public DebugBlockTransmutePacket() {
    }

    public DebugBlockTransmutePacket(FriendlyByteBuf buffer) {
        this();
    }

    public void encode(FriendlyByteBuf buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ctx.get().enqueueWork(() -> {
                ServerPlayer player = ctx.get().getSender();
                if (player == null || !(player.containerMenu instanceof DebugBlockContainer menu)) {
                    return;
                }

                BlockPos pos = menu.getPos();
                if (player.level.getBlockEntity(pos) instanceof DebugBlockEntity blockEntity) {
                    blockEntity.transmute();
                }
            });
            ctx.get().setPacketHandled(true);
        });

        ctx.get().setPacketHandled(true);
    }
}
