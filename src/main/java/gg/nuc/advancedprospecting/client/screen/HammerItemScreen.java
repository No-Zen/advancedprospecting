package gg.nuc.advancedprospecting.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.container.HammerItemContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HammerItemScreen extends AbstractContainerScreen<HammerItemContainer> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(AdvancedProspecting.MOD_ID, "textures/gui/hammer_item.png");
    private final HammerItemContainer container;
    private SimpleListWidget listWidget;
    private Integer levelY = null;
    private String materialText = null;
    private Block selectedBlock = null;

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

        //ContainerData data = container.getData();
        //String yLevel = data.get(0) != 0 ? String.valueOf(data.get(0)) : "???";
        //String material = data.get(1) != 0 ? String.valueOf(data.get(1)) : "???";

        if (selectedBlock != null) {
            ItemStack itemStack = new ItemStack(selectedBlock);

            // Get the ItemRenderer and BakedModel
            ItemRenderer itemRenderer = this.minecraft.getItemRenderer();
            BakedModel bakedModel = itemRenderer.getModel(itemStack, null, null, 0);

            // Set up the render environment
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            // Set up the PoseStack for rendering
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.translate(this.leftPos + 8.0D + 32, this.topPos + 55.0D + 32, 100.0D);
            modelViewStack.scale(64.0F, -64.0F, 64.0F); // Scale 4 times larger
            RenderSystem.applyModelViewMatrix();

            // Set up the lighting and render the item
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            Lighting.setupForFlatItems();

            itemRenderer.render(itemStack, ItemTransforms.TransformType.GUI, false, stack, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
            bufferSource.endBatch();

            // Clean up the render environment
            RenderSystem.enableDepthTest();
            Lighting.setupFor3DItems();

            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }

        this.font.draw(stack, "Y level: " + (levelY != null ? levelY : "-"), this.leftPos + 7, this.topPos + 32, 0x404040);
        //this.font.draw(stack, "Material: " + (materialText != null ? materialText : "-"), this.leftPos + 7, this.topPos + 43, 0x404040);

        this.font.draw(stack, this.title, this.leftPos + 7, this.topPos + 7, 0x404040);
        this.font.draw(stack, this.playerInventoryTitle, this.leftPos + 7, this.topPos + 124, 0x404040);

        this.renderTooltip(stack, mouseX, mouseY);
        if (selectedBlock != null) {
            if (mouseX >= this.leftPos + 8 && mouseY >= this.topPos + 55 && mouseX <= this.leftPos + 8 + 64 && mouseY <= this.topPos + 55 + 64) {
                minecraft.screen.renderTooltip(stack, new TextComponent(selectedBlock.getName().getString()), mouseX, mouseY);
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        if (listWidget == null) {
            listWidget = new SimpleListWidget(0, 0, 64, 100);

            int worldMinY = Minecraft.getInstance().player.level.getMinBuildHeight();
            int worldMaxY = Minecraft.getInstance().player.level.getMaxBuildHeight();
            int current = 0;
            BlockState prev = null;
            List<RockLayerWidget> widgets = new ArrayList<>();

            for (int i = worldMaxY; i >= worldMinY; i--) {
                BlockPos pos = new BlockPos(Minecraft.getInstance().player.getX(), i, Minecraft.getInstance().player.getZ());
                BlockState blockState = Minecraft.getInstance().player.level.getBlockState(pos);

                RockLayerWidget widget = new RockLayerWidget(blockState, pos, 0, 0, listWidget.getWidth(), listWidget.getWidth() / 16, wid -> {
                    //String msg = "B " + (index + 1);
                    //AdvancedProspecting.LOGGER.warn(msg);
                    this.selectedBlock = wid.getBlock();
                    this.levelY = wid.getPos().getY();
                    this.materialText = wid.getMaterialText();
                });

                widgets.add(widget);
                listWidget.addWidget(widget);

                if (prev == null) {
                    prev = blockState;
                    current = 1;
                } else if (blockState.equals(prev)) {
                    current += 1;
                } else {
                    for (int j = 0; j < current; j++) {
                        int index = widgets.size() - 1 - current + j;
                        widgets.get(index).setLayerIndex(j);
                        widgets.get(index).setLayerHeight(current);
                    }
                    prev = blockState;
                    current = 1;
                }
            }

            for (int j = 0; j < current; j++) {
                int index = widgets.size() - current + j;
                widgets.get(index).setLayerIndex(j);
                widgets.get(index).setLayerHeight(current);
            }

            for (RockLayerWidget widget : widgets) {
                widget.adjustLayer();
            }

            {

                //final int index = i;
                //TextComponent widgetTitle = new TextComponent("I ÅÍj_ " + (i + 1));
                //int he = (int) ((Math.random() * (30 - 10)) + 10);
                /*
                ExtendedButton widget = new ExtendedButton(0, 0, listWidget.getWidth(), he, widgetTitle, button -> {
                    String msg = "B " + (index + 1);
                    LOGGER.warn(msg);
                });
                 */
                /*
                RockLayerWidget widget = new RockLayerWidget(blockState, pos, 0, 0, listWidget.getWidth(), listWidget.getWidth()/16, wid -> {
                    //String msg = "B " + (index + 1);
                    //AdvancedProspecting.LOGGER.warn(msg);
                    this.selectedBlock=wid.getBlock();
                    this.levelY = wid.getPos().getY();
                    this.materialText = wid.getMaterialText();
                });
                listWidget.addWidget(widget);
                */
            }
        }
        // Update the position and size of the existing listWidget to match the new window size
        listWidget.x = this.leftPos + 86;
        listWidget.y = this.topPos + 19;

        addRenderableWidget(listWidget);
    }

    /*
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        listWidget.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
    */

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        listWidget.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        listWidget.mouseReleased(mouseX, mouseY, button);
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