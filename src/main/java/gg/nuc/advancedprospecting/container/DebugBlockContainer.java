package gg.nuc.advancedprospecting.container;

import gg.nuc.advancedprospecting.blockentities.DebugBlockEntity;
import gg.nuc.advancedprospecting.container.syncdata.DebugBlockContainerData;
import gg.nuc.advancedprospecting.init.BlockInit;
import gg.nuc.advancedprospecting.init.ContainerInit;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class DebugBlockContainer extends AbstractContainerMenu {

    public final BlockPos pos;
    private final ContainerLevelAccess containerAccess;
    public final ContainerData data;

    // Client Constructor
    public DebugBlockContainer(int id, Inventory playerInv) {
        this(id, playerInv, new ItemStackHandler(2), BlockPos.ZERO, new SimpleContainerData(4));
    }

    // Server constructor
    public DebugBlockContainer(int id, Inventory playerInv, IItemHandler slots, BlockPos pos, ContainerData data) {
        super(ContainerInit.DEBUG_BLOCK.get(), id);
        this.pos=pos;
        this.containerAccess = ContainerLevelAccess.create(playerInv.player.level, pos);
        this.data = data;

        final int SLOT_SIZE_WITH_EDGE = 16 + 2;
        final int PLAYER_INVENTORY_X = 8;
        final int PLAYER_INVENTORY_Y = 72;
        final int PLAYER_HOTBAR_Y = 130;

        final Slot INPUT_SLOT = addSlot(new InputSlot(slots, 0, 62, 18));
        final Slot OUTPUT_SLOT = addSlot(new OutputSlot(slots, 1, 134, 18));

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(playerInv, 9 + row * 9 + column, PLAYER_INVENTORY_X + column * SLOT_SIZE_WITH_EDGE, PLAYER_INVENTORY_Y + row * SLOT_SIZE_WITH_EDGE));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInv, column, PLAYER_INVENTORY_X + column * SLOT_SIZE_WITH_EDGE, PLAYER_HOTBAR_Y));
        }

        addDataSlots(data);
    }

    private class InputSlot extends SlotItemHandler {
        public InputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return stack.is(ItemTags.PLANKS);
        }
    }

    private class OutputSlot extends SlotItemHandler {

        public OutputSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return false;
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.containerAccess, player, BlockInit.DEBUG_BLOCK.get());
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        var retStack = ItemStack.EMPTY;
        final Slot slot = getSlot(index);
        if (slot.hasItem()) {
            final ItemStack item = slot.getItem();
            retStack = item.copy();
            if (index < 1) {
                if (!moveItemStackTo(item, 1, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if (!moveItemStackTo(item, 0, 1, false)) return ItemStack.EMPTY;

            if (item.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return retStack;
    }

    public static MenuConstructor getServerContainer(DebugBlockEntity be, BlockPos pos) {
        return (id, playerInv, player) -> new DebugBlockContainer(id, playerInv, be.inventory, pos, new DebugBlockContainerData(be, 4));
    }

    public BlockPos getPos() {
        return pos;
    }
}
