package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.tuple.Pair;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day17 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        Pattern pattern = Pattern.compile("target area: x=(\\d+)\\.\\.(\\d+), y=([-]\\d+)\\.\\.([-]\\d+)");
        final Matcher matcher = pattern.matcher(stringInput.get(0));

        matcher.find();

        int Tx1 = Integer.parseInt(matcher.group(1));    //20
        int Tx2 = Integer.parseInt(matcher.group(2));    //30
        int Ty1 = Integer.parseInt(matcher.group(3));    //-10
        int Ty2 = Integer.parseInt(matcher.group(4));    // -5

        assert Tx1 < Tx2 && Ty1 < Ty2;


        int x0 = 0;
        int y0 = 0;

        int v0min, v0max;

        int v0 = 0;
        int nmax = 0;

        Set<Pair<Integer, Integer>> locations = new HashSet<>();

        for (int n = 1; n < 10000; n++) {

            v0min = BigDecimal.valueOf((2L*Ty1 + (long) n*(n-1)), 0)
                    .divide(BigDecimal.valueOf(2L*n), RoundingMode.CEILING)
                    .intValue();

            v0max = BigDecimal.valueOf((2L*Ty2 + (long) n*(n-1)), 0)
                    .divide(BigDecimal.valueOf(2L*n), RoundingMode.FLOOR)
                    .intValue();

            if (v0min <= v0max) {
                for (int v = v0min; v <= v0max; v++) {

                    for (int u = 1; u < 1000; u++) {

                        int y = y0 + n*v - n*(n-1)/2;
                        int x = x0;
                        for (int m = 0; m < n; m++) {
                            if (u <= m) break;
                            x += u - m;
                        }

                        if (Tx1 <= x && x <= Tx2 && Ty1 <= y && y <= Ty2) {
                            v0 = Math.max(v0, v);
                            nmax = n;
                            locations.add(Pair.of(u, v));
                        }
                    }
                }
            }
        }


        int maxy = 0;
        for (int n = 1; n < nmax; n++) {
            maxy = Math.max(maxy, y0 + n*v0 - n*(n-1)/2);
        }

        System.out.println("Result Part 1: "+maxy);
        System.out.println("Result Part 2: "+locations.size());
    }

}

