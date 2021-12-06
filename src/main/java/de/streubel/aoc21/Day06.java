package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;

public class Day06 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        long sum;

        sum = getSum(stringInput, 80);
        System.out.println("Result Part 1: "+ sum);

        sum = getSum(stringInput, 256);
        System.out.println("Result Part 2: "+ sum);
    }

    private long getSum(List<String> stringInput, int N) {
        long[] ages = new long[9];

        Arrays.stream(stringInput.get(0).split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .forEach(age -> ages[age]++);

        for (int n = 0; n < N; n++) {
            final long left = rotateLeft(ages);
            ages[6] += left;
            ages[8] += left;
        }

        return Arrays.stream(ages).sum();
    }

    private static long rotateLeft(long[] array) {
        final long mostLeft = array[0];
        System.arraycopy(array, 1, array, 0, array.length - 1);
        array[8] = 0;
        return mostLeft;
    }
}
