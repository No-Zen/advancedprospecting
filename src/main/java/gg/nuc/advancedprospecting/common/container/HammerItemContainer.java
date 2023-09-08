package gg.nuc.advancedprospecting.common.container;

import gg.nuc.advancedprospecting.common.container.syncdata.HammerItemContainerData;
import gg.nuc.advancedprospecting.core.init.ContainerInit;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class HammerItemContainer extends AbstractContainerMenu {
    public final ContainerData data;

    // Client Constructor
    public HammerItemContainer(int id, Inventory playerInv) {
        //FIXME Bad stupid code
        this(id, playerInv, new ItemStackHandler(2), Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND), Minecraft.getInstance().player, new SimpleContainerData(4));
    }

    //FIXME What about slots, itemStack and player?
    // Server constructor
    public HammerItemContainer(int id, Inventory playerInv, IItemHandler slots, ItemStack itemStack, Player player, ContainerData data) {
        super(ContainerInit.HAMMER_ITEM.get(), id);
        this.data = data;

        final int SLOT_SIZE_WITH_EDGE = 16 + 2;
        final int PLAYER_INVENTORY_X = 8;
        final int PLAYER_INVENTORY_Y = 72;
        final int PLAYER_HOTBAR_Y = 130;

        //TODO Lock slot to stop player from removing item that opened GUI

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

    public static MenuConstructor getServerContainer(ItemStack itemStack, Player player) {
        return (id, playerInv, playerEntity) -> new HammerItemContainer(id, playerInv, new ItemStackHandler(2), itemStack, player, new HammerItemContainerData(4));
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
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
}
