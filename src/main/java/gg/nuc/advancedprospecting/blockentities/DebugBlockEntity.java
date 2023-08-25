package gg.nuc.advancedprospecting.blockentities;

import gg.nuc.advancedprospecting.init.BlockEntityInit;
import gg.nuc.advancedprospecting.network.ModNetwork;
import gg.nuc.advancedprospecting.network.SyncBlockEntityPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.PacketDistributor;

public class DebugBlockEntity extends BlockEntity {
    public DebugBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DEBUG_BLOCK.get(), pos, state);
    }

    int ticks = 0;

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be_) {
        DebugBlockEntity be = (DebugBlockEntity) be_;

        if (!level.isClientSide()) {
            be.ticks++;

            if (be.ticks > 20) {
                be.ticks = 0;

                TextComponent textComponent = new TextComponent("Tick at " + pos);
                for (ServerPlayer player : ((ServerLevel) level).players()) {
                    player.sendMessage(textComponent, player.getUUID());
                }
            }

            CompoundTag nbt = be.saveWithoutMetadata();
            SyncBlockEntityPacket packet = new SyncBlockEntityPacket(be.getBlockPos(), nbt);
            ModNetwork.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> be.getLevel().getChunkAt(be.getBlockPos())), packet);
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("ticks", this.ticks);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.ticks = nbt.getInt("ticks");
    }
}
