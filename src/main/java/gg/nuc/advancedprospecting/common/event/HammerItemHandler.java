package gg.nuc.advancedprospecting.common.event;

import gg.nuc.advancedprospecting.common.container.HammerItemContainer;
import gg.nuc.advancedprospecting.common.item.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;

import java.util.Random;

public class HammerItemHandler {
    public HammerItemHandler() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void handleRightClickEvent(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getPlayer();
        ItemStack heldItem = player.getItemInHand(event.getHand());
        Level level = event.getWorld();
        BlockPos blockPos = event.getPos();

        if (heldItem.getItem() instanceof HammerItem) {
            if (level.isClientSide) {
                if (!player.isCrouching()) {
                    player.swing(event.getHand());
                    player.getCooldowns().addCooldown(heldItem.getItem(), 5);
                    level.playSound(player, player.getX(), player.getY(), player.getZ(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.0F);

                    TextComponent textComponent = new TextComponent("");
                    textComponent.append(new TextComponent("Block: " + blockPos));

                    player.sendMessage(textComponent, player.getUUID());
                }
            } else {
                if (player.isCrouching()) {
                    MenuProvider container = new SimpleMenuProvider(HammerItemContainer.getServerContainer(heldItem, player), HammerItem.TITLE);
                    NetworkHooks.openGui((ServerPlayer) player, container);
                } else {
                    player.swing(event.getHand());
                    player.getCooldowns().addCooldown(heldItem.getItem(), 5);

                    Random random = new Random();
                    int randomYLevel = random.nextInt(100) + 1;

                    // Get the existing NBT data or create a new one if it doesn't exist
                    CompoundTag nbt = heldItem.getOrCreateTag();
                    nbt.putInt("YLevel", randomYLevel);
                }
            }

            event.setCanceled(true);
        }
    }
}
