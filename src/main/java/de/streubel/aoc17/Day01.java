package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.Arrays;
import java.util.List;

public class Day01 extends AdventOfCodeRunner {
    public void run(List<String> stringInput) {
        char[] digitInput = stringInput.get(0)
                .toCharArray();

        for (int offset: Arrays.asList(1, digitInput.length/2)) {
            int sum = 0;
            for (int i = 0; i < digitInput.length; i++) {
                char c = digitInput[i];

                if (c == digitInput[(i + offset)%digitInput.length]) {
                    sum += Integer.parseInt("" + c);
                }
            }
            System.out.println(sum);
        }
    }
}
