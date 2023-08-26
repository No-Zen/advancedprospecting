package gg.nuc.advancedprospecting.common.block;

import gg.nuc.advancedprospecting.common.block.entity.DebugBlockEntity;
import gg.nuc.advancedprospecting.common.container.DebugBlockContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DebugBlock extends Block implements EntityBlock {
    public DebugBlock(Block.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DebugBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return level.isClientSide() ? null : (level_, pos_, state_, blockEntity_) -> ((DebugBlockEntity) blockEntity_).tick();
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof DebugBlockEntity be) {
            MenuProvider container = new SimpleMenuProvider(DebugBlockContainer.getServerContainer(be, pos), DebugBlockEntity.TITLE);
            NetworkHooks.openGui((ServerPlayer) player, container, pos);
        }
        return InteractionResult.SUCCESS;
    }


    @Override
    public void randomTick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull Random rand) {
        if (!level.isClientSide()) {
            BlockEntity be_ = level.getBlockEntity(pos);
            if (be_ instanceof DebugBlockEntity be) {
                be.randomTick();
            }
        }
    }

    @Override
    public void onRemove(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean moving) {
        final BlockEntity be_ = level.getBlockEntity(pos);
        if (!((be_ instanceof final DebugBlockEntity be))) return;

        for (int slot = 0; slot < be.inventory.getSlots(); slot++) {
            if (be.inventory.getStackInSlot(slot).isEmpty()) return;

            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, be.inventory.getStackInSlot(slot)));
        }

        super.onRemove(state, level, pos, newState, moving);
    }
}
