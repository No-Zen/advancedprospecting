package gg.nuc.advancedprospecting.common.block.entity;

import gg.nuc.advancedprospecting.AdvancedProspecting;
import gg.nuc.advancedprospecting.common.block.entity.util.InventoryBlockEntity;
import gg.nuc.advancedprospecting.core.init.BlockEntityInit;
import gg.nuc.advancedprospecting.core.init.PacketHandler;
import gg.nuc.advancedprospecting.core.network.SyncBlockEntityPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class DebugBlockEntity extends InventoryBlockEntity implements BlockEntityTicker<DebugBlockEntity> {
    public static final Component TITLE = new TranslatableComponent("container." + AdvancedProspecting.MOD_ID + ".debug_block");
    private final int max_progress = 20 * 3;
    private int ticks = 0;
    private int randomTicks = 0;
    private int progress = 0;
    private boolean active = false;

    public DebugBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DEBUG_BLOCK.get(), pos, state, 2);
    }

    @Override
    public void tick() {
        this.ticks++;

        if (this.ticks >= 1200) {
            this.ticks = 0;
        }

        if (this.active) {
            if (this.getItemInSlot(0).isEmpty() || this.getItemInSlot(1).getCount() >= this.getItemInSlot(1).getMaxStackSize()) {
                this.active = false;
            } else {
                this.progress++;
                if (this.progress >= this.max_progress) {
                    this.active = false;
                    this.getItemInSlot(0).shrink(1);
                    if (this.getItemInSlot(1).isEmpty()) {
                        this.insertItem(1, new ItemStack(Items.DIAMOND));
                    } else {
                        this.getItemInSlot(1).grow(1);
                    }
                }
            }
        }
        if (!this.active) {
            this.progress = 0;
        }

        this.progress++;

        if (this.progress > this.max_progress) {
            this.progress = 0;
        }

        CompoundTag nbt = this.saveWithoutMetadata();
        SyncBlockEntityPacket packet = new SyncBlockEntityPacket(this.getBlockPos(), nbt);
        PacketHandler.CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> this.getLevel().getChunkAt(this.getBlockPos())), packet);

        super.tick();
    }

    public void randomTick() {
        this.randomTicks++;
    }

    public void transmute() {
        this.active = true;
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, DebugBlockEntity be) {
        be.tick();
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("ticks", this.ticks);
        //nbt.putInt("progress", this.progress);
        //nbt.putBoolean("active", this.active);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.ticks = nbt.getInt("ticks");
        //this.progress = nbt.getInt("progress");
        //this.active = nbt.getBoolean("active");
    }

    public int getTicks() {
        return this.ticks;
    }

    public int getRandomTicks() {
        return this.randomTicks;
    }

    public int getProgress() {
        return this.progress;
    }

    public boolean isActive() {
        return this.active;
    }

    public int getMaxProgress() {
        return this.max_progress;
    }
}
