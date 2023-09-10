package gg.nuc.advancedprospecting.client.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.ArrayList;
import java.util.List;

public class SimpleListWidget extends AbstractWidget {
    private final List<String> items = new ArrayList<>();
    private int offset = 0;
    private int selected = -1;

    public SimpleListWidget(int x, int y, int width, int height) {
        super(x, y, width, height, new TranslatableComponent(""));
    }

    public void addItem(String item) {
        items.add(item);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        // Draw background
        fill(matrices, x, y, x + width, y + height, 0xFF000000);

        // Draw items
        for (int i = 0; i < items.size(); i++) {
            int entryY = y + i * 10 - offset;
            if (entryY > y - 10 && entryY < y + height) {
                if (i == selected) {
                    fill(matrices, x, entryY, x + width, entryY + 10, 0x880000FF);
                }
                Minecraft.getInstance().font.draw(matrices, items.get(i), x + 2, entryY + 2, 0xFFFFFFFF);
            }
        }

        // Draw scrollbar
        int maxOffset = items.size() * 10 - height;
        if (maxOffset > 0) {
            int scrollbarHeight = (int) ((float) height / items.size());
            int scrollbarY = (int) ((float) offset / maxOffset * (height - scrollbarHeight));
            fill(matrices, x + width - 6, y + scrollbarY, x + width - 2, y + scrollbarY + scrollbarHeight, 0x80FFFFFF);
        } else {
            fill(matrices, x + width - 6, y, x + width - 2, y + height, 0x80CCCCCC);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            selected = ((int) mouseY - y + offset) / 10;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int maxOffset = items.size() * 10 - height;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            offset = (int) Math.max(0, Math.min(offset - amount * 10, maxOffset));
            return true;
        }
        return false;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        // Implement narration update logic, if necessary
    }
}