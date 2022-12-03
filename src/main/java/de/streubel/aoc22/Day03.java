package de.streubel.aoc22;

import com.google.common.collect.Sets;
import com.google.common.primitives.Chars;
import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day03 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final Integer resultPart1 = stringInput.stream()
                .map(s -> Pair.of(s.substring(0, s.length() / 2), s.substring(s.length() / 2)))
                .map(pair -> Sets.intersection(
                        new HashSet<>(Chars.asList(pair.getLeft().toCharArray())),
                        new HashSet<>(Chars.asList(pair.getRight().toCharArray()))
                      ).stream().findFirst().get()
                )
                .map(item -> Character.isLowerCase(item) ? item - 'a' + 1 : item - 'A' + 27)
                .reduce(0, Integer::sum);

        System.out.println("Result Part 1 (7980): "+ resultPart1);


        final AtomicInteger index = new AtomicInteger(0);
        final int resultPart2 = stringInput.stream()
                .collect(Collectors.groupingBy(cdm -> index.getAndIncrement() / 3))
                .values()
                .stream()
                .map(strings -> {
                    final int[] occurances = new int[52];

                    final Set<Character> chars = new HashSet<>();
                    for (String s : strings) {
                        chars.addAll(Chars.asList(s.toCharArray()));
                    }

                    for (char c : chars) {
                        for (String s : strings) {
                            if (s.indexOf(c) != -1) {
                                occurances[prioritizeItem(c)-1]++;
                            }
                        }
                    }

                    for (int i = 0; i < occurances.length; i++) {
                        if (occurances[i] == 3) {
                            return i+1;
                        }
                    }
                    return 0;
                })
                .reduce(0, Integer::sum);

        System.out.println("Result Part 2 (2881): "+resultPart2);

    }

    private static int prioritizeItem(char item) {
        return Character.isLowerCase(item) ? item - 'a' + 1 : item - 'A' + 27 ;
    }
}
