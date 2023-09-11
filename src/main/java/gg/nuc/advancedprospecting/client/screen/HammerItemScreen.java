package gg.nuc.advancedprospecting.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.container.HammerItemContainer;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import org.jetbrains.annotations.NotNull;

import static gg.nuc.advancedprospecting.AdvancedProspecting.LOGGER;

public class HammerItemScreen extends AbstractContainerScreen<HammerItemContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdvancedProspecting.MOD_ID, "textures/gui/hammer_item.png");
    private final HammerItemContainer container;
    private SimpleListWidget listWidget;

    public HammerItemScreen(HammerItemContainer container, Inventory playerInv, Component title) {
        super(container, playerInv, title);
        this.container = container;
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

        ContainerData data = container.getData();
        String yLevel = data.get(0) != 0 ? String.valueOf(data.get(0)) : "???";
        String material = data.get(1) != 0 ? String.valueOf(data.get(1)) : "???";

        this.font.draw(stack, "Y level: " + yLevel, this.leftPos + 7, this.topPos + 32, 0x404040);
        this.font.draw(stack, "Material: " + material, this.leftPos + 7, this.topPos + 43, 0x404040);

        this.font.draw(stack, this.title, this.leftPos + 7, this.topPos + 7, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 7, this.topPos + 124, 0x404040);

        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();

        listWidget = new SimpleListWidget(this.leftPos + 86, this.topPos + 19, 64, 100);

        for (int i = 0; i < 15; i++) {
            final int index = i;
            TextComponent widgetTitle = new TextComponent("I ÅÍj_ " + (i + 1));
            int he = (int) ((Math.random() * (30 - 10)) + 10);
            ExtendedButton widget = new ExtendedButton(0, 0, listWidget.getWidth(), he, widgetTitle, button -> {
                LOGGER.warn("B " + (index + 1));
            });
            listWidget.addWidget(widget);
        }

        addRenderableWidget(listWidget);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
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