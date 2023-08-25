package gg.nuc.advancedprospecting.event;

import gg.nuc.advancedprospecting.AdvancedProspectingMain;
import gg.nuc.advancedprospecting.init.ContainerInit;
import gg.nuc.advancedprospecting.screen.DebugBlockScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AdvancedProspectingMain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientModEvents {

    private ClientModEvents() {
    }

    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {
        MenuScreens.register(ContainerInit.DEBUG_BLOCK.get(), DebugBlockScreen::new);
    }
}
