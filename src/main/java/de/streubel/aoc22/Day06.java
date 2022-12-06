package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final char[] datastream = stringInput.get(0).toCharArray();

        final int N_Part1 = 4;
        final int N_Part2 = 14;

        final HashSet<Character> characters = new HashSet<>();

        Integer startOfPacketCounterPart1 = null;
        Integer startOfPacketCounterPart2 = null;

        for (int i = 0; i < datastream.length; i++) {

            // Part1
            characters.clear();
            if (startOfPacketCounterPart1 == null) {
                for (int j = 0; j < N_Part1; j++) {
                    characters.add(datastream[i+j]);
                }
                if (characters.size() == N_Part1) {
                    startOfPacketCounterPart1 = i + N_Part1;
                }
            }


            // Part2
            characters.clear();
            if (startOfPacketCounterPart2 == null) {
                for (int j = 0; j < N_Part2; j++) {
                    characters.add(datastream[i+j]);
                }
                if (characters.size() == N_Part2) {
                    startOfPacketCounterPart2 = i + N_Part2;
                }
            }

            if (startOfPacketCounterPart1 != null && startOfPacketCounterPart2 != null) {
                break;
            }
        }


        System.out.println("Result Part 1 (1140): " + startOfPacketCounterPart1);
        System.out.println("Result Part 2 (3495): " + startOfPacketCounterPart2);

    }
}
