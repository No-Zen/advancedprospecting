package gg.nuc.advancedprospecting.common.container.syncdata;

import gg.nuc.advancedprospecting.common.block.entity.DebugBlockEntity;
import net.minecraft.world.inventory.SimpleContainerData;

public class DebugBlockContainerData extends SimpleContainerData {
    private final DebugBlockEntity blockEntity;

    public DebugBlockContainerData(DebugBlockEntity be, int amount) {
        super(amount);
        this.blockEntity = be;
    }

    @Override
    public int get(int key) {
        return switch (key) {
            case 0 -> this.blockEntity.getProgress();
            case 1 -> this.blockEntity.getMaxProgress();
            case 2 -> this.blockEntity.getTicks();
            case 3 -> this.blockEntity.getRandomTicks();
            default -> throw new UnsupportedOperationException("Unable to get key: '" + key + "' for block entity: '"
                    + this.blockEntity + "' at pos: '" + this.blockEntity.getBlockPos() + "'");
        };
    }

    public DebugBlockEntity getBlockEntity() {
        return this.blockEntity;
    }
}
