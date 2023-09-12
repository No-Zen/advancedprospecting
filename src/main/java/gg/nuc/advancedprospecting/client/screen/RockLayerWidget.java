package gg.nuc.advancedprospecting.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import gg.nuc.advancedprospecting.core.util.LightLevel;
import gg.nuc.advancedprospecting.core.util.MathN;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RockLayerWidget extends AbstractWidget {
    private static final Map<ResourceLocation, TextureAtlasSprite> spriteCache = new HashMap<>();
    private static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation BLOCKS_TEXTURE = new ResourceLocation("minecraft", "textures/atlas/blocks.png");
    private static final MultiBufferSource.BufferSource bufferSource = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
    private static final RenderType renderType = RenderType.translucent();
    private final BlockPos pos;
    private BlockState blockState;
    private Block block;
    private ResourceLocation material;
    private String materialText;
    private int layerIndex;
    private int layerHeight;
    private List<Component> tooltip;
    private Consumer<RockLayerWidget> lambda;

    public RockLayerWidget(BlockState blockState, BlockPos pos, int x, int y, int width, int height, Consumer<RockLayerWidget> lambda) {
        super(x, y, width, height, new TranslatableComponent(""));
        this.blockState = blockState;

        this.block = this.blockState.getBlock();
        this.material = this.block.getRegistryName();
        this.materialText = this.block.getName().getString();

        this.pos = pos;
        this.lambda = lambda;
    }

    public static void drawBlock(PoseStack poseStack, int layerIndex, int layerHeight, ResourceLocation blockRegistryName, float x, float y, float width, float height) {
        TextureAtlasSprite sprite = spriteCache.computeIfAbsent(blockRegistryName, RockLayerWidget::getSpriteForBlock);
        RenderSystem.setShaderTexture(0, BLOCKS_TEXTURE);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Use the static bufferSource and renderType
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        Matrix4f matrix = poseStack.last().pose();

        float _minU = sprite.getU0(); // Left?
        float _maxU = sprite.getU1(); // Right?
        float _minV = sprite.getV0(); // Top
        float _maxV = sprite.getV1(); // Bottom

        //TODO Add tooltip for preview block
        //TODO Maybe display "unknown" text for Unknown?
        //TODO Appropriate texture for unknown?

        float minU = _minU;
        float maxU = _maxU;
        float minV = (float) MathN.map(16.0 / layerHeight * layerIndex, 0, 16, _minV, _maxV);
        float maxV = (float) MathN.map(16.0 / layerHeight * (layerIndex + 1), 0, 16, _minV, _maxV);

        int blockLight = LightLevel.FULLBRIGHT.block;
        int skyLight = LightLevel.FULLBRIGHT.sky;

        vertexConsumer.vertex(matrix, x, y + height, 0).color(255, 255, 255, 255).uv(minU, maxV).uv2(blockLight, skyLight).normal(0, 0, 1f).endVertex();
        vertexConsumer.vertex(matrix, x + width, y + height, 0).color(255, 255, 255, 255).uv(maxU, maxV).uv2(blockLight, skyLight).normal(0, 0, 1f).endVertex();
        vertexConsumer.vertex(matrix, x + width, y, 0).color(255, 255, 255, 255).uv(maxU, minV).uv2(blockLight, skyLight).normal(0, 0, 1f).endVertex();
        vertexConsumer.vertex(matrix, x, y, 0).color(255, 255, 255, 255).uv(minU, minV).uv2(blockLight, skyLight).normal(0, 0, 1f).endVertex();


        bufferSource.endBatch();
    }

    private static TextureAtlasSprite getSpriteForBlock(ResourceLocation blockRegistryName) {
        Block block = ForgeRegistries.BLOCKS.getValue(blockRegistryName);
        if (block == null) {
            // Log a warning message (optional)
            return minecraft.getTextureAtlas(BLOCKS_TEXTURE).apply(MissingTextureAtlasSprite.getLocation());
        }
        BlockState blockState = block.defaultBlockState();
        return minecraft.getBlockRenderer().getBlockModel(blockState).getParticleIcon();
    }

    public Block getBlock() {
        return block;
    }

    public void setLayerIndex(int layerIndex) {
        this.layerIndex = layerIndex;
    }

    public void setLayerHeight(int layerHeight) {
        this.layerHeight = layerHeight;
    }

    public void adjustLayer() {
        int totalCount = this.layerHeight > 0 ? this.layerHeight : 1;
        int index = this.layerIndex;
        int maxSize = 16;

        int groupCount = (int) Math.ceil((double) totalCount / maxSize);
        int baseSize = totalCount / groupCount;
        int remainder = totalCount - (baseSize * groupCount);

        if (index < remainder * (baseSize + 1)) {
            this.layerHeight = baseSize + 1;
        } else {
            this.layerHeight = baseSize;
        }
        int prev;
        if (index + 1 <= (baseSize + 1) * remainder) {
            prev = ((int) Math.ceil((double) (index + 1) / (baseSize + 1)) - 1) * (baseSize + 1);
        } else {
            //prev=((int)Math.ceil((double)(index+1)/(totalCount-(baseSize+1)*remainder))-1)*baseSize;
            prev = (baseSize + 1) * remainder + ((int) Math.ceil((double) ((index + 1) - (baseSize + 1) * remainder) / baseSize) - 1) * baseSize;
        }

        this.layerIndex -= prev;


        if (!(this.blockState.is(Tags.Blocks.STONE) || this.blockState.isAir() || this.blockState.is(Blocks.BEDROCK))) {
            this.blockState = null;
            this.materialText = "Unknown";
            this.block = null;
            this.material = null;
        } else if (this.blockState.isAir()) {
            this.materialText = "Air";
        }

        this.tooltip = new ArrayList<>();
        tooltip.add(new TextComponent("Y: " + this.pos.getY()));
        tooltip.add(new TextComponent(this.materialText));
    }

    public String getMaterialText() {
        return materialText;
    }

    public BlockPos getPos() {
        return pos;
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);

        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.lambda.accept(this);
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        //super.render(stack, mouseX, mouseY, delta);
        //fill(stack, x, y, x + width - (int) (Math.random() * 20), y + height - 1, 0x40FF00FF);

        if (this.blockState == null) {
            fill(stack, x, y, x + width, y + height, 0xFF000000);
        } else if (this.blockState.isAir()) {
            //Fill with sky color
            Level world = minecraft.level;
            if (world != null) {
                Player player = minecraft.player;
                if (player != null) {
                    BlockPos playerPos = player.blockPosition();
                    Biome biome = world.getBiome(playerPos).value();
                    //long timeOfDay = world.getDayTime() % 24000;
                    float angle = world.getSunAngle(delta);

                    // Determine the base colors for day and night.
                    int dayColor = biome.getSkyColor();
                    int nightColor = 0x000022; // A dark blue color for the night sky

                    // Calculate a factor to use for interpolating between the day and night colors
                    float factor = 1 - (float) (Math.cos(angle) + 1) / 2.0f;

                    // Interpolate between the day and night colors based on the factor
                    int interpolatedColor =
                            (0xFF << 24) | // Setting alpha to FF
                                    ((int) ((1 - factor) * ((dayColor >> 16) & 0xFF) + factor * ((nightColor >> 16) & 0xFF)) << 16) |
                                    ((int) ((1 - factor) * ((dayColor >> 8) & 0xFF) + factor * ((nightColor >> 8) & 0xFF)) << 8) |
                                    ((int) ((1 - factor) * (dayColor & 0xFF) + factor * (nightColor & 0xFF)));

                    fill(stack, x, y, x + width, y + height, interpolatedColor);
                } else {
                    fill(stack, x, y, x + width, y + height, 0xFF000000);
                }
            } else {
                fill(stack, x, y, x + width, y + height, 0xFF000000);
            }
        } else {
            if (this.blockState != null) {
                drawBlock(stack, this.layerIndex, this.layerHeight, this.material, x, y, width, height);
            }
        }
    }

    public void renderToolTip(PoseStack stack, int mouseX, int mouseY) {
        //FIXME Should be translated
        minecraft.screen.renderComponentTooltip(stack, this.tooltip, mouseX, mouseY);
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput output) {
        // Implement narration update logic, if necessary
    }
}
