package gg.nuc.advancedprospecting.network;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;

public class SyncBlockEntityPacket {

    private final BlockPos pos;
    private final CompoundTag nbt;

    public SyncBlockEntityPacket(BlockPos pos, CompoundTag nbt) {
        this.pos = pos;
        this.nbt = nbt;
    }

    public BlockPos getPos() {
        return pos;
    }

    public CompoundTag getNbt() {
        return nbt;
    }
}

