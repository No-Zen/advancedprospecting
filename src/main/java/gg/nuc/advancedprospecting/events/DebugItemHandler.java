package gg.nuc.advancedprospecting.events;

import gg.nuc.advancedprospecting.items.DebugItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class DebugItemHandler {
    public DebugItemHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getItemInHand(event.getHand());
        Level level = event.getWorld();
        BlockPos blockPos = event.getPos();

        if (heldItem.getItem() instanceof DebugItem) {
            player.swing(event.getHand());
            level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (level.isClientSide) {
                BlockState blockState = level.getBlockState(blockPos);
                BlockEntity blockEntity = level.getBlockEntity(blockPos);

                TextComponent textComponent = new TextComponent("");
                textComponent.append(new TextComponent("Debug information\n")
                        .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.BOLD).withStyle(ChatFormatting.UNDERLINE));
                textComponent.append(new TextComponent("\nBlock State:")
                        .withStyle(ChatFormatting.GRAY));
                textComponent.append(new TextComponent("\n" + blockState));
                textComponent.append(new TextComponent("\n\nBlock Entity:")
                        .withStyle(ChatFormatting.GRAY));
                textComponent.append(new TextComponent("\n" + (blockEntity != null ? blockEntity.saveWithFullMetadata().toString() : "No block entity")));

                player.sendMessage(textComponent, player.getUUID());
            }

            event.setCanceled(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onGuiOpen(ScreenOpenEvent event) {
        if (event.getScreen() instanceof AbstractContainerScreen && !(event.getScreen() instanceof EffectRenderingInventoryScreen)) {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                ItemStack heldItem = player.getMainHandItem();

                if (!heldItem.isEmpty() && heldItem.getItem() instanceof DebugItem) {
                    event.setCanceled(true);
                }
            }
        }
    }
}
