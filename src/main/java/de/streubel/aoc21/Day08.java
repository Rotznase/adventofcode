package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Day08 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final String[] segmentPatterns1 = {
                "cagedb",  // "0" (length 6)
                "ab",      // "1" (length 2)
                "gcdfa",   // "2" (length 5)
                "fbcad",   // "3" (length 5)
                "eafb",    // "4" (length 4)
                "cdfbe",   // "5" (length 5)
                "cdfgeb",  // "6" (length 6)
                "dab",     // "7" (length 3)
                "acedgfb", // "8" (length 7)
                "cefabd"   // "9" (length 6)
        };

        int nrOfEasyDigits = 0;
        for (String line : stringInput) {
            final String[] parts = line.split("\\|");
            final String[] uniqueSignalPatterns = parts[0].trim().split(" ");
            final String[] fourDigitOutputValue = parts[1].trim().split(" ");

            nrOfEasyDigits += stream(fourDigitOutputValue)
                    .mapToInt(String::length)
                    .filter(length -> length == segmentPatterns1[1].length() ||
                                      length == segmentPatterns1[4].length() ||
                                      length == segmentPatterns1[7].length() ||
                                      length == segmentPatterns1[8].length())
                    .count();
        }
        System.out.println("Result Part 1: "+nrOfEasyDigits);


        int sumOfOutput = 0;
        for (String line : stringInput) {
            final String[] parts = line.split("\\|");
            final String[] uniqueSignalPatterns = parts[0].trim().split(" ");
            final String[] fourDigitOutputValue = parts[1].trim().split(" ");

            final String[] segmentPatterns2 = new String[10];


            final String s1 = stream(uniqueSignalPatterns)
                    .filter(s -> s.length() == 2)
                    .findFirst()
                    .get();
            segmentPatterns2[1] = sortChars(s1);

            final String s7 = stream(uniqueSignalPatterns)
                    .filter(s -> s.length() == 3)
                    .findFirst()
                    .get();
            segmentPatterns2[7] = sortChars(s7);

            final String s4 = stream(uniqueSignalPatterns)
                    .filter(s -> s.length() == 4)
                    .findFirst()
                    .get();
            segmentPatterns2[4] = sortChars(s4);

            final String s8 = stream(uniqueSignalPatterns)
                    .filter(s -> s.length() == 7)
                    .findFirst()
                    .get();
            segmentPatterns2[8] = sortChars(s8);

            final Collection<String> s235 = stream(uniqueSignalPatterns)
                    .filter(s -> s.length() == 5)
                    .collect(Collectors.toList());

            final Collection<String> s069 = stream(uniqueSignalPatterns)
                    .filter(s -> s.length() == 6)
                    .collect(Collectors.toList());


            final String bd = s4.replaceAll("["+s1+"]", "");

            s235.forEach(s -> {
                if (containsAnyOf(s, s1)) {
                    segmentPatterns2[3] = sortChars(s);
                } else if (containsAnyOf(s, bd)) {
                    segmentPatterns2[5] = sortChars(s);
                } else {
                    segmentPatterns2[2] = sortChars(s);
                }
            });

            
            final String eb = segmentPatterns2[8].replaceAll("["+segmentPatterns2[3]+"]", "");

            s069.forEach(s -> {
                if (containsAnyOf(s, segmentPatterns2[3] + segmentPatterns2[4])) {
                    segmentPatterns2[9] = sortChars(s);
                } else if (containsAnyOf(s, segmentPatterns2[5] + eb)) {
                    segmentPatterns2[6] = sortChars(s);
                } else {
                    segmentPatterns2[0] = sortChars(s);
                }
            });


            final Map<String, Integer> segmentsToDigit = stream(segmentPatterns2)
                    .collect(Collectors.toMap(Function.identity(), s -> Arrays.asList(segmentPatterns2).indexOf(s)));

            int singleOutput = 0;
            for (String s : fourDigitOutputValue) {
                final int digit = segmentsToDigit.get(sortChars(s));
                singleOutput = 10 * singleOutput + digit;
            }

            sumOfOutput += singleOutput;
        }

        System.out.println("Result Part 2: "+sumOfOutput);
    }

    private boolean containsAnyOf(String s, String containment) {
        for (char c : containment.toCharArray()) {
            if (s.indexOf(c) == -1) {
                return false;
            }
        }
        return true;
    }

    private String sortChars(String s) {
        final char[] a = s.toCharArray();
        Arrays.sort(a);
        return new String(a);
    }
}
