package gg.nuc.advancedprospecting.blocks;

import gg.nuc.advancedprospecting.blockentities.DebugBlockEntity;
import gg.nuc.advancedprospecting.container.DebugBlockContainer;
import gg.nuc.advancedprospecting.init.BlockEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
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
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class DebugBlock extends Block implements EntityBlock {
    public DebugBlock(Block.Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return BlockEntityInit.DEBUG_BLOCK.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return type == BlockEntityInit.DEBUG_BLOCK.get() ? DebugBlockEntity::tick : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {

        if (level.isClientSide()) {
            TextComponent textComponent = new TextComponent("Interact at " + pos);
            player.sendMessage(textComponent, player.getUUID());
        }

        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof DebugBlockEntity be) {
            MenuProvider container = new SimpleMenuProvider(DebugBlockContainer.getServerContainer(be, pos), DebugBlockEntity.TITLE);
            NetworkHooks.openGui((ServerPlayer) player, container, pos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random rand) {
        if (!level.isClientSide()) {
            TextComponent textComponent = new TextComponent("Random tick at " + pos);
            for (ServerPlayer player : level.players()) {
                player.sendMessage(textComponent, player.getUUID());
            }
        }
    }
}
