package gg.nuc.advancedprospecting.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.container.DebugBlockContainer;
import gg.nuc.advancedprospecting.core.init.PacketHandler;
import gg.nuc.advancedprospecting.core.network.DebugBlockTransmutePacket;
import gg.nuc.advancedprospecting.core.util.MathN;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.widget.ExtendedButton;
import org.jetbrains.annotations.NotNull;

public class DebugBlockScreen extends AbstractContainerScreen<DebugBlockContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdvancedProspecting.MOD_ID, "textures/gui/debug_block.png");

    private ExtendedButton transmuteButton;

    public DebugBlockScreen(DebugBlockContainer container, Inventory playerInv, Component title) {
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

        final int progress = this.menu.data.get(0);
        final int maxProgress = this.menu.data.get(1);
        final int scaledWidth = (int) MathN.mapClamped(progress, 0, maxProgress, 0, 22);
        bindTexture();
        blit(stack, this.leftPos + 95, this.topPos + 18, 176, 0, scaledWidth, 16);

        final int ticks = this.menu.data.get(2);
        final int randomTicks = this.menu.data.get(3);
        this.font.draw(stack, "T: " + ticks, this.leftPos + 8, this.topPos + 25, 0x404040);
        this.font.draw(stack, "R: " + randomTicks, this.leftPos + 8, this.topPos + 40, 0x404040);

        this.font.draw(stack, this.title, this.leftPos + 8, this.topPos + 5, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 8, this.topPos + 61, 0x404040);

        this.renderTooltip(stack, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        this.transmuteButton = addRenderableWidget(new ExtendedButton(this.leftPos + 61, this.topPos + 39, 90, 18, new TranslatableComponent("button." + AdvancedProspecting.MOD_ID + ".transmute"), btn -> PacketHandler.CHANNEL.sendToServer(new DebugBlockTransmutePacket())));
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