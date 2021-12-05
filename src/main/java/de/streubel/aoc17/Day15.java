package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.List;

public class Day15 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
//        int inputA = 65;
//        int inputB = 8921;
        int inputA = 699;
        int inputB = 124;
        Generator generatorA;
        Generator generatorB;

        // Teil 1
        generatorA = new Generator(16807, inputA, 1);
        generatorB = new Generator(48271, inputB, 1);
        int matchCounter = 0;
        for (int i=0; i<40_000_000; i++) {
            generatorA.next();
            generatorB.next();
            if (generatorA.first16Bit().equals(generatorB.first16Bit()))
                matchCounter++;
        }
        System.out.println(matchCounter);

        // Teil 2
        generatorA = new Generator(16807, inputA, 4);
        generatorB = new Generator(48271, inputB, 8);
        matchCounter = 0;
        for (int i=0; i<5_000_000; i++) {
            generatorA.next();
            generatorB.next();
            if (generatorA.first16Bit().equals(generatorB.first16Bit()))
                matchCounter++;
        }
        System.out.println(matchCounter);
    }

    private class Generator {
        long factor;
        long value;
        int filter;

        Generator(int factor, int start, int filter) {
            this.factor = factor;
            this.value = start;
            this.filter = filter;
        }

        void next() {
            do {
                value = value * factor % 2147483647;
            } while (value % filter != 0);
        }

        int getValue() {
            return (int) value;
        }

        String first16Bit() {
            return Integer.toBinaryString((int) value & 0xFFFF);
        }
    }
}
