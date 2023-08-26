package gg.nuc.advancedprospecting.core.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBlockEntityPacket {

    private final BlockPos pos;
    private final CompoundTag nbt;

    public SyncBlockEntityPacket(BlockPos pos, CompoundTag nbt) {
        this.pos = pos;
        this.nbt = nbt;
    }

    public SyncBlockEntityPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readNbt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeNbt(this.nbt);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Level clientWorld = Minecraft.getInstance().level;

            if (clientWorld != null) {
                BlockEntity be = clientWorld.getBlockEntity(this.pos);
                if (be != null) {
                    be.load(this.nbt);
                }
            }
        });

        ctx.get().setPacketHandled(true);
    }
}

