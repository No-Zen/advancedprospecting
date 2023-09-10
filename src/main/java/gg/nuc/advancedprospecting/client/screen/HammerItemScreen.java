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
    }

    public static void bindTexture() {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.setShaderTexture(0, TEXTURE);
    }

    @Override
    public void render(@NotNull PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        super.render(stack, mouseX, mouseY, partialTicks);

        this.font.draw(stack, this.title, this.leftPos + 8, this.topPos + 5, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 8, this.topPos + 61, 0x404040);

        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();

        SimpleListWidget listWidget = new SimpleListWidget(this.leftPos + 8, this.topPos + 18, this.imageWidth - 16, 40);
        listWidget.addItem("Item 1");
        listWidget.addItem("Item 2");
        listWidget.addItem("Item 3");
        listWidget.addItem("Item 4");
        listWidget.addItem("Item 5");
        listWidget.addItem("Item 6");
        listWidget.addItem("Item 7");

        addRenderableWidget(listWidget);
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