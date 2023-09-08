package gg.nuc.advancedprospecting.common.container.syncdata;

import net.minecraft.world.inventory.SimpleContainerData;

public class HammerItemContainerData extends SimpleContainerData {

    public HammerItemContainerData(int amount) {
        super(amount);
    }

    /*
    @Override
    public int get(int key) {
        return switch (key) {
            case 0 -> this.blockEntity.getProgress();
            case 1 -> this.blockEntity.getMaxProgress();
            case 2 -> this.blockEntity.getTicks();
            case 3 -> this.blockEntity.getRandomTicks();
            default -> throw new UnsupportedOperationException("Unable to get key: '" + key + "' for block entity");
        };
    }
    */
}
