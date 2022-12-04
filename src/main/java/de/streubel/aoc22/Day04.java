package de.streubel.aoc22;

import com.google.common.collect.Range;
import de.streubel.AdventOfCodeRunner;

import java.util.List;

public class Day04 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        int resultPart1 = 0;
        int resultPart2 = 0;

        for (String line : stringInput) {
            final String[] rangeString = line.split(",");

            final Range<Integer> r1 = parseRange(rangeString[0]);
            final Range<Integer> r2 = parseRange(rangeString[1]);

            if (r1.encloses(r2) || r2.encloses(r1)) {
                resultPart1++;
            }

            try {
                r1.intersection(r2);
                resultPart2++;
            } catch (Exception ignored) {
            }

        }

        System.out.println("Result Part 1 (498): "+ resultPart1);
        System.out.println("Result Part 2 (859): "+resultPart2);
    }

    private static Range<Integer> parseRange(String line) {
        final String[] ranges = line.split("-");
        return Range.closed(Integer.valueOf(ranges[0]), Integer.valueOf(ranges[1]));
    }
}
