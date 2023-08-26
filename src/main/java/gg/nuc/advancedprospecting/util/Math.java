package gg.nuc.advancedprospecting.util;

public class Math {
    public static double clamp(double value, double min, double max) {
        return value < min ? min : value > max ? max : value;
    }

    public static double map(double value, double rangeMin, double rangeMax, double resultMin, double resultMax) {
        return (value - rangeMin) / (rangeMax - rangeMin) * (resultMax - resultMin) + resultMin;
    }

    public static double mapClamped(double value, double rangeMin, double rangeMax, double resultMin, double resultMax) {
        double clampedValue=Math.clamp(value,rangeMin,rangeMax);
        return (value - rangeMin) / (rangeMax - rangeMin) * (resultMax - resultMin) + resultMin;
    }
}
