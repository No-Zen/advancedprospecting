package gg.nuc.advancedprospecting.client.event;

import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.client.screen.DebugBlockScreen;
import gg.nuc.advancedprospecting.client.screen.HammerItemScreen;
import gg.nuc.advancedprospecting.core.init.ContainerInit;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AdvancedProspecting.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerInit.DEBUG_BLOCK.get(), DebugBlockScreen::new);
        MenuScreens.register(ContainerInit.HAMMER_ITEM.get(), HammerItemScreen::new);
    }
}
