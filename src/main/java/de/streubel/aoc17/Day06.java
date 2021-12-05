package de.streubel.aoc17;

import com.google.common.base.Joiner;
import de.streubel.AdventOfCodeRunner;

import java.util.*;

public class Day06 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        Integer[] membanks = {4,10,4,1,8,4,9,14,5,1,14,15,0,15,3,5};
        Map<String, Integer> redistris = new HashMap<>();

        Integer firstCycle;
        int cylcles = 0;
        do {
            final int indexMaxBlocks = getIndexMaxBlocks(membanks);
            int blocks = membanks[indexMaxBlocks];
            membanks[indexMaxBlocks] = 0;

            int index = (indexMaxBlocks + 1) % membanks.length;
            while (blocks > 0) {
                membanks[index] += 1;
                blocks--;
                index = (index + 1) % membanks.length;
            }
            cylcles++;
            firstCycle = redistris.put(hash(membanks), cylcles);
        } while (firstCycle == null);

        System.out.println(cylcles);
        System.out.println(cylcles-firstCycle);
    }

    private static int getIndexMaxBlocks(Integer[] membanks) {
        final List<Integer> list = Arrays.asList(membanks);
        Integer max = Collections.max(list);
        return list.indexOf(max);
    }

    private static String hash(Integer[] intArray) {
        return Joiner.on(",").join(intArray);
    }
}
