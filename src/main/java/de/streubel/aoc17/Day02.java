package de.streubel.aoc17;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;
import de.streubel.AdventOfCodeRunner;

import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

public class Day02 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {

        int sum1 = 0;
        int sum2 = 0;
        for (String line : stringInput) {
            final SortedSet<Integer> transform = FluentIterable.from(Arrays.asList(line.split("\\s+")))
                    .transform(new Function<String, Integer>() {
                        @Override
                        public Integer apply(final String input) {
                            return Integer.parseInt(input);
                        }
                    })
                    .toSortedSet(Ordering.<Integer>natural());

            // Teil 1:
            int min = transform.first();
            int max = transform.last();
            int diff = max - min;
            sum1 += diff;

            // Teil 2:
            Integer[] array = transform.toArray(new Integer[transform.size()]);
            for (int i=0; i<array.length-1; i++) {
                for (int j=i+1; j<array.length; j++) {
                    if (array[j] % array[i] == 0) {
                        sum2 += array[j] / array[i];
                    }
                }
            }
        }

        System.out.println(sum1);
        System.out.println(sum2);
    }
}
