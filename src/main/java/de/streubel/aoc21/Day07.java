package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.List;
import java.util.function.IntToLongFunction;
import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static java.util.Arrays.stream;

public class Day07 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        int[] hPos = stream(stringInput.get(0).split(","))
                .mapToInt(Integer::parseInt).toArray();

        int minPos = stream(hPos).min().getAsInt();
        int maxPos = stream(hPos).max().getAsInt();

        Long minCostSum;

        minCostSum = IntStream.range(minPos, maxPos)
                .mapToLong(h -> stream(hPos).mapToLong(costLinear(h)).sum())
                .min().getAsLong();
        System.out.println("Result Part 1: "+minCostSum);

        minCostSum = IntStream.range(minPos, maxPos)
                .mapToLong(h -> stream(hPos).mapToLong(costSquare(h)).sum())
                .min().getAsLong();
        System.out.println("Result Part 2: "+minCostSum);
    }

    private IntToLongFunction costSquare(int h) {
        return hh -> (long) abs(h - hh) * (abs(h - hh) + 1) / 2;
    }

    private IntToLongFunction costLinear(int h) {
        return hh -> abs(h - hh);
    }
}
