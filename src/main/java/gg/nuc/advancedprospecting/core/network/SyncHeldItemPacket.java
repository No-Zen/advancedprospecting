package gg.nuc.advancedprospecting.core.network;

import gg.nuc.advancedprospecting.common.container.HammerItemContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncHeldItemPacket {

    private final ItemStack itemStack;

    public SyncHeldItemPacket(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public SyncHeldItemPacket(FriendlyByteBuf buffer) {
        this(buffer.readItem());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;

            if (player == null || !(player.containerMenu instanceof HammerItemContainer menu)) {
                return;
            }

            menu.updateContainerWithItemStack(this.itemStack);
        });

        ctx.get().setPacketHandled(true);
    }
}