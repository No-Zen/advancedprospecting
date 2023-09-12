package gg.nuc.advancedprospecting.core.util;

@SuppressWarnings("unused")
public class MathN {
    public static double clamp(double value, double min, double max) {
        return java.lang.Math.max(min, java.lang.Math.min(value, max));
    }

    public static double map(double value, double rangeMin, double rangeMax, double resultMin, double resultMax) {
        return (value - rangeMin) / (rangeMax - rangeMin) * (resultMax - resultMin) + resultMin;
    }

    public static double mapClamped(double value, double rangeMin, double rangeMax, double resultMin, double resultMax) {
        double clampedValue = MathN.clamp(value, rangeMin, rangeMax);
        return (clampedValue - rangeMin) / (rangeMax - rangeMin) * (resultMax - resultMin) + resultMin;
    }
}
