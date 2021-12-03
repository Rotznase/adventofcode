package de.streubel.aoc21;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Day03 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final List<Integer> report = stringInput.stream()
                .map(s -> Integer.parseInt(s, 2))
                .collect(Collectors.toList());

        final int N = stringInput.get(0).length();

        int gammaRate = 0;
        int epsilonRate;

        for (int pos = N-1; pos >= 0; pos--) {
            int count_1 = 0;
            for (Integer i : report) {
                if (getBit(i, pos) != 0) {
                    count_1++;
                }
            }

            if (2*count_1 > stringInput.size()) {
                gammaRate = setBit(gammaRate, pos);
            }

        }

        epsilonRate = (1 << N)-1 & ~gammaRate;
        int powerConsumption = gammaRate * epsilonRate;

        System.out.println("Result Part 1: "+powerConsumption);



        int oxygenGeneratorRating = bitCriteria(report, N, true);
        int CO2ScrubberRating = bitCriteria(report, N, false);
        int lifeSupportRating = oxygenGeneratorRating * CO2ScrubberRating;

        System.out.println("Result Part 2: "+lifeSupportRating);

    }

    private Integer bitCriteria(List<Integer> report, int N, final boolean mostCommon) {
        List<Integer> x = new ArrayList<>(report);

        for (int pos = N -1; pos >= 0; pos--) {

            final int _pos = pos;

            Multimap<Integer, Integer> y = Multimaps.index(x, i -> (i >> _pos) & 1);

            int searchBit;
            if (mostCommon) {
                searchBit = y.get(1).size() >= y.get(0).size() ? 1 : 0;
            } else {
                searchBit = y.get(1).size() >= y.get(0).size() ? 0 : 1;
            }

            x = x.stream()
                 .filter(i -> ((i >> _pos) & 1) == searchBit)
                 .collect(Collectors.toList());

            if (x.size() <= 1) {
                break;
            }
        }

        return x.get(0);
    }

    private static int getBit(int b, int position) {
        return (1 << position) & b;
    }

    private static int setBit(int b, int position) {
        return (1 << position) | b;
    }
}
