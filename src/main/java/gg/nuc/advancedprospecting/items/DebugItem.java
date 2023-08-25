package gg.nuc.advancedprospecting.items;

import gg.nuc.advancedprospecting.events.DebugItemHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DebugItem extends Item {
    static DebugItemHandler debugItemHandler = new DebugItemHandler();

    public DebugItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_41427_) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        // Check if the tooltip list isn't empty and contains the name
        if (!tooltip.isEmpty()) {
            // Replace the first component (the name of the item) with a colored version
            tooltip.set(0, tooltip.get(0).copy().withStyle(ChatFormatting.GOLD)); // For example, set it to red
        }

        // Add tooltip
        tooltip.add(new TextComponent("Gives information about stuff"));

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
