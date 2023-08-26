package gg.nuc.advancedprospecting.core.init;

import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.item.DebugItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, AdvancedProspecting.MOD_ID);

    public static final RegistryObject<Item> DEBUG_ITEM = ITEMS.register("debug_item", () -> new DebugItem(new Item.Properties().tab(ModCreativeTab.instance)));

    public static class ModCreativeTab extends CreativeModeTab {
        public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "advancedprospecting");

        private ModCreativeTab(int index, String label) {
            super(index, label);
        }

        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(DEBUG_ITEM.get());
        }
    }
}
