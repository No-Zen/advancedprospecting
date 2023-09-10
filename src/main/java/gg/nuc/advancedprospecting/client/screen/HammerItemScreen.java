package gg.nuc.advancedprospecting.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.container.HammerItemContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class HammerItemScreen extends AbstractContainerScreen<HammerItemContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdvancedProspecting.MOD_ID, "textures/gui/hammer_item.png");

    public HammerItemScreen(HammerItemContainer container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
        this.leftPos = 0;
        this.topPos = 0;
        this.imageWidth = 176;
        this.imageHeight = 218;
    }

    public static void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        this.font.draw(stack, "Y level: ???", this.leftPos + 7, this.topPos + 32, 0x404040);
        this.font.draw(stack, "Material: ???", this.leftPos + 7, this.topPos + 43, 0x404040);

        this.font.draw(stack, this.title, this.leftPos + 7, this.topPos + 7, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 7, this.topPos + 124, 0x404040);


        this.renderTooltip(stack, mouseX, mouseY);
    }

    private SimpleListWidget listWidget;

    @Override
    protected void init() {
        super.init();

        listWidget = new SimpleListWidget(this.leftPos + 86, this.topPos + 19, 64, 100);

        for (int i = 0; i < 15; i++) {
            listWidget.addItem("Item ÅÍJG_ " + (i + 1));
        }

        addRenderableWidget(listWidget);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        // Assuming simpleListWidget is your SimpleListWidget instance
        if (listWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (listWidget.mouseReleased(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float mouseX, int mouseY, int partialTicks) {
        renderBackground(stack);
        bindTexture();
        blit(stack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int mouseX, int mouseY) {
    }
}