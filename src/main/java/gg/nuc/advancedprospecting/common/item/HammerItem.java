package gg.nuc.advancedprospecting.common.item;

import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.event.HammerItemHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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

public class HammerItem extends Item {
    public static final Component TITLE = new TranslatableComponent("container." + AdvancedProspecting.MOD_ID + ".hammer_item");
    static HammerItemHandler hammerItemHandler = new HammerItemHandler();

    public HammerItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, player.getItemInHand(hand));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (!tooltip.isEmpty()) {
            tooltip.set(0, tooltip.get(0).copy().withStyle(ChatFormatting.GRAY));
        }

        tooltip.add(new TranslatableComponent("tooltip." + AdvancedProspecting.MOD_ID + ".hammer_item"));

        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }
}
