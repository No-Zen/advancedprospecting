package gg.nuc.advancedprospecting.core.init;

import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.container.DebugBlockContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ContainerInit {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, AdvancedProspecting.MOD_ID);

    public static final RegistryObject<MenuType<DebugBlockContainer>> DEBUG_BLOCK = CONTAINERS.register("debug_block", () -> new MenuType<>(DebugBlockContainer::new));
}
