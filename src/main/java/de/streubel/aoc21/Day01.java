package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.Arrays;
import java.util.List;

public class Day01 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {
        int[] input = new int[stringInput.size()];
        for (int i=0; i<input.length; i++) {
            input[i] = Integer.parseInt(stringInput.get(i));
        }


        int current = input[0];
        int countIncreased = 0;
        for (int i = 1; i < input.length; i++) {
            final int x = input[i];

            if (current < x) {
                countIncreased++;
            }
            current = x;
        }
        System.out.println("Result Part 1: "+ countIncreased);


        final int windowLength = 3;
        countIncreased = 0;
        current = measurementWindow(input, 0, windowLength);
        for (int i = 1; i < input.length-windowLength+1; i++) {
            final int x = measurementWindow(input, i, windowLength);

            if (current < x) {
                countIncreased++;
            }
            current = x;
        }
        System.out.println("Result Part 2: "+countIncreased);


    }

    private static int measurementWindow(final int[] input, final int firstIndex, final int windowLength) {
        int sum = 0;
        for (int i = firstIndex; i < firstIndex + windowLength; i++) {
            sum += input[i];
        }
        return sum;
    }
}
