package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day02 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {


        Pattern pattern = Pattern.compile("(\\w+) (\\d+)");

        long forward = 0;
        long depth = 0;
        for (String move : stringInput) {
            final Matcher matcher = pattern.matcher(move);
            if (matcher.matches()) {
                final String direction = matcher.group(1);
                final long length = Long.parseLong(matcher.group(2));
                switch (direction) {
                    case "forward": forward += length; break;
                    case "down":    depth   += length; break;
                    case "up":      depth   -= length; break;
                }
            }
        }

        long result1 = forward * depth;
        System.out.println("Result Part 1: "+result1);


        forward = 0;
        depth = 0;
        long aim = 0;
        for (String move : stringInput) {
            final Matcher matcher = pattern.matcher(move);
            if (matcher.matches()) {
                final String direction = matcher.group(1);
                final long length = Long.parseLong(matcher.group(2));
                switch (direction) {
                    case "forward": forward += length;
                                    depth += aim*length; break;
                    case "down":    aim     += length; break;
                    case "up":      aim     -= length; break;
                }
            }
        }

        long result2 = forward * depth;
        System.out.println("Result Part 2: "+result2);

    }
}
