package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.ArrayUtils.toObject;


public class Day14 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        char[] polymer = stringInput.get(0).toCharArray();

        Map<String, Character> pairInsertions = stringInput.stream()
                .skip(2)
                .collect(toMap(
                        line -> line.split(" -> ")[0],
                        line -> line.split(" -> ")[1].toCharArray()[0]
                ));


        int[] N = {10,40};     // Achtung! dauert lange
        for (int n = 0; n < N.length; n++) {

            Counter[] frequencies = new Counter[26];
            for (int c = 0; c < frequencies.length; c++) {
                frequencies[c] = new Counter(0);
            }

            Map<Character, Long> x = Arrays.stream(toObject(polymer)).collect(groupingBy(identity(), counting()));

            for (Map.Entry<Character, Long> p : x.entrySet()) {
                frequencies[p.getKey()-'A'].setCount(p.getValue());
            }

            for (int p = 0; p < polymer.length - 1; p++) {
                insertPolymer(polymer[p], polymer[p+1], pairInsertions, frequencies, N[n]);
            }

            long maxFrequency = Arrays.stream(frequencies).filter(Counter::notNull).mapToLong(Counter::getCount).max().orElse(0L);
            long minFrequency = Arrays.stream(frequencies).filter(Counter::notNull).mapToLong(Counter::getCount).min().orElse(0L);

            System.out.println("Result Part "+n+": "+(maxFrequency-minFrequency));
        }
    }

    private static void insertPolymer(char left, char right,
                                      Map<String, Character> pairInsertions,
                                      Counter[] frequencies,
                                      int depth) {

        if (depth > 0) {
            char middle = pairInsertions.get(left + "" + right);

            insertPolymer(left, middle, pairInsertions, frequencies, depth - 1);
            insertPolymer(middle, right, pairInsertions, frequencies, depth - 1);

            frequencies[middle-'A'].increment();
        }
    }

    private static class Counter {
        long count;

        public Counter(long count) {
            this.count = count;
        }

        public long getCount() {
            return count;
        }

        public void setCount(long count) {
            this.count = count;
        }

        public void increment() {
            count++;
        }

        public boolean notNull() {
            return count != 0;
        }
        
        @Override
        public String toString() {
            return ""+count;
        }
    }
}
