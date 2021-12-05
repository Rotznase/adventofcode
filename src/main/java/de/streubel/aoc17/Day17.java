package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.*;

public class Day17 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        final int maxCycles = 2018;
        final int step = 344;

        // Teil 1
        final List<Integer> buffer = new ArrayList<>(maxCycles);
        int insertAfterPos;

        buffer.add(0);
        insertAfterPos = 0;

        for (int currentValue=1; currentValue<maxCycles; currentValue++) {
            final int o = buffer.size() - (insertAfterPos + 1);
            Collections.rotate(buffer, o);
            buffer.add(currentValue);
            Collections.rotate(buffer, -o);
            insertAfterPos = (insertAfterPos + 1 + step) % buffer.size();
        }

        final int currentPos = buffer.indexOf(2017);
        final int result = buffer.get((currentPos+1)%buffer.size());
        System.out.println(result);

        // Teil 2
        int valueAfterPos0 = 0;
        insertAfterPos = 0;
        for (int currentValue=1; currentValue<50_000_000; currentValue++) {
            if (insertAfterPos == 0) {
                valueAfterPos0 = currentValue;
            }
            insertAfterPos = (insertAfterPos + 1 + step) % (currentValue + 1);
        }
        System.out.println(valueAfterPos0);
    }

 }
