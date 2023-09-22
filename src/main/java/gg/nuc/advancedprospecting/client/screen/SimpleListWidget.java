package gg.nuc.advancedprospecting.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.core.util.Render;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleListWidget extends AbstractWidget {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdvancedProspecting.MOD_ID, "textures/gui/list_gui.png");
    private final List<AbstractWidget> items = new ArrayList<>();
    private int offset = 0;
    private boolean isDragging = false;

    public SimpleListWidget(int x, int y, int width, int height) {
        //FIXME This shouldn't be blank right?
        super(x, y, width, height, new TranslatableComponent(""));
    }

    public static void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    public void addWidget(AbstractWidget widget) {
        items.add(widget);
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float delta) {
        super.render(stack, mouseX, mouseY, delta);

        //Bottom left & top right corners
        fill(stack, x - 1, y - 1, (x - 1) + (width + 2), (y - 1) + (height + 2), 0xFF8B8B8B);
        //Left & top sides
        fill(stack, x - 1, y - 1, (x - 1) + (width + 1), (y - 1) + (height + 1), 0xFF373737);
        //Right & bottom sides
        fill(stack, x, y, x + (width + 1), y + (height + 1), 0xFFFFFFFF);
        //Main area
        fill(stack, x, y, x + width, y + height, 0xFF8B8B8B);

        int scrollbarX = x + width + 6;
        //Bottom left & top right corners
        fill(stack, scrollbarX - 1, y - 1, (scrollbarX - 1) + (12 + 2), (y - 1) + (height + 2), 0xFF8B8B8B);
        //Left & top sides
        fill(stack, scrollbarX - 1, y - 1, (scrollbarX - 1) + (12 + 1), (y - 1) + (height + 1), 0xFF373737);
        //Right & bottom sides
        fill(stack, scrollbarX, y, scrollbarX + (12 + 1), y + (height + 1), 0xFFFFFFFF);
        //Main area
        fill(stack, scrollbarX, y, scrollbarX + 12, y + height, 0xFF8B8B8B);

        // Draw items, a list of sub-widgets e.g. buttons.
        Render.enableScissor(x, y, width, height);
        int totalHeight = 0;
        for (AbstractWidget widget : items) {
            int entryY = y + totalHeight - offset;
            totalHeight += widget.getHeight();
            widget.x = x;
            widget.y = entryY;
            if (entryY > y - widget.getHeight() && entryY < y + height) {
                widget.render(stack, mouseX, mouseY, delta);
            }
        }
        Render.disableScissor();

        // Draw scrollbar
        bindTexture();
        int maxOffset = totalHeight - height;
        if (maxOffset > 0) {
            int scrollerY = (int) (y + (float) (y + height - 15 - y) / (maxOffset) * offset);
            //blit(stack, destX, destY, srcX, srcY, srcW, srcH, atlasW, atlasH)
            blit(stack, scrollbarX, scrollerY, 0, 0, 12, 15, 32, 32);
        } else {
            blit(stack, scrollbarX, y, 12, 0, 12, 15, 32, 32);
        }

        for (AbstractWidget widget : items) {
            if (this.isMouseOver(mouseX, mouseY) && widget.isMouseOver(mouseX, mouseY)) {
                widget.renderToolTip(stack, mouseX, mouseY);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isMouseOver(mouseX, mouseY)) {
            for (AbstractWidget widget : items) {
                if (widget.isMouseOver(mouseX, mouseY)) {
                    widget.onClick(mouseX, mouseY);
                }
            }
        }

        int totalHeight = 0;
        for (AbstractWidget widget : items) {
            totalHeight += widget.getHeight();
        }

        int maxOffset = totalHeight - height;
        int scrollbarHeight = 15;
        int scrollerY = (int) (y + (float) (y + height - scrollbarHeight - y) / (maxOffset) * offset);

        if (mouseX >= x + width + 6 && mouseX <= x + width + 6 + 12 && mouseY >= scrollerY && mouseY <= scrollerY + scrollbarHeight) {
            this.setFocused(true);
            isDragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        int totalHeight = 0;
        for (AbstractWidget widget : items) {
            totalHeight += widget.getHeight();
        }

        if (isDragging) {
            int maxOffset = totalHeight - height;
            int scrollbarHeight = 15;
            offset = (int) ((mouseY - y - scrollbarHeight / 2.0) / (height - scrollbarHeight) * maxOffset);
            offset = Math.max(0, Math.min(offset, maxOffset));
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        isDragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int totalHeight = 0;
        for (AbstractWidget widget : items) {
            totalHeight += widget.getHeight();
        }

        int maxOffset = totalHeight - height;
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
            offset = (int) Math.max(0, Math.min(offset - amount * 10, maxOffset));
            return true;
        }
        return false;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput output) {
        // Implement narration update logic, if necessary
    }
}