package gg.nuc.advancedprospecting.core.util;

public enum LightLevel {
    UNLIT(0 << 4, 0 << 4),
    FULLBRIGHT(15 << 4, 15 << 4),
    NORMAL(0 << 4, 15 << 4),
    DAY(7 << 4, 15 << 4),
    NIGHT(7 << 4, 0 << 4),
    L0(0 << 4, 0 << 4),
    L1(1 << 4, 1 << 4),
    L2(2 << 4, 2 << 4),
    L3(3 << 4, 3 << 4),
    L4(4 << 4, 4 << 4),
    L5(5 << 4, 5 << 4),
    L6(6 << 4, 6 << 4),
    L7(7 << 4, 7 << 4),
    L8(8 << 4, 8 << 4),
    L9(9 << 4, 9 << 4),
    L10(10 << 4, 10 << 4),
    L11(11 << 4, 11 << 4),
    L12(12 << 4, 12 << 4),
    L13(13 << 4, 13 << 4),
    L14(14 << 4, 14 << 4),
    L15(15 << 4, 15 << 4);

    public final int block;
    public final int sky;

    LightLevel(int block, int sky) {
        this.block = block;
        this.sky = sky;
    }
}