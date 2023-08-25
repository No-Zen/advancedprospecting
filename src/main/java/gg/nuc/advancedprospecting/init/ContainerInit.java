package gg.nuc.advancedprospecting.init;

import gg.nuc.advancedprospecting.AdvancedProspectingMain;
import gg.nuc.advancedprospecting.blocks.DebugBlock;
import gg.nuc.advancedprospecting.container.DebugBlockContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, AdvancedProspectingMain.MOD_ID);

    public static final RegistryObject<MenuType<DebugBlockContainer>> DEBUG_BLOCK = CONTAINERS.register("debug_block", () -> new MenuType<>(DebugBlockContainer::new));
}
