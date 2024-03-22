package pl.daveproject.dietapp;

public class UnitConverter {
    public static double roundToTwoDecimalDigits(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public static int roundToInt(double value) {
        return (int) Math.round(value * 100);
    }
}
