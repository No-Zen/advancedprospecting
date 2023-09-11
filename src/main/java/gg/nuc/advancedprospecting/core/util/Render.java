package gg.nuc.advancedprospecting.core.util;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;

public class Render {
    public static void enableScissor(int x, int y, int width, int height) {
        Window window = Minecraft.getInstance().getWindow();
        double scaleFactor = window.getGuiScale();
        int screenHeight = window.getScreenHeight();

        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);
        int scaledX = (int) (x * scaleFactor);
        int scaledY = (int) (screenHeight - (y + height) * scaleFactor);

        RenderSystem.enableScissor(scaledX, scaledY, scaledWidth, scaledHeight);
    }

    public static void disableScissor() {
        RenderSystem.disableScissor();
    }
}
