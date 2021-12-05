package de.streubel.aoc17;

public abstract class AdventOfCode {

    public static void main(String[] args) throws Exception {
        int dayNr;

        for (dayNr = 1; dayNr <= 25; dayNr++) {
            final String className = String.format("de.streubel.aoc17.Day%02d", dayNr);
            final Class<?> clazz = Class.forName(className);
            AdventOfCode runner = (AdventOfCode) clazz.newInstance();
            runner.run();
        }
    }

    abstract void run();
}
