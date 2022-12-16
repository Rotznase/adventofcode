package de.streubel.aoc22;

import com.google.common.collect.Range;
import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class Day15 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        boolean example = false;

        final Range<Integer> probeAreaXPart1;
        final Range<Integer> probeAreaXPart2;
        final Range<Integer> probeAreaYPart1;
        final Range<Integer> probeAreaYPart2;

        if (example) {
            // Part 1
            probeAreaXPart1 = Range.all();
            probeAreaYPart1 = Range.singleton(10);
            // Part 2
            probeAreaXPart2 = Range.closed(0, 20);
            probeAreaYPart2 = Range.closed(0, 20);
        } else {
            // Part 1
            probeAreaXPart1 = Range.all();
            probeAreaYPart1 = Range.singleton(2000000);
            // Part 2
            probeAreaXPart2 = Range.closed(0, 4000000);
            probeAreaYPart2 = Range.closed(0, 4000000);
        }

        final Map<Position, Position> sensorToBeacon = parseSensorsAndBeacons(stringInput);

        Set<Position> coveredRegionPart;

        coveredRegionPart = getCoveredRegion(probeAreaXPart1, probeAreaYPart1, sensorToBeacon);

        coveredRegionPart.removeAll(sensorToBeacon.values());


        int resultPart1 = coveredRegionPart.size();
        if (example) {
            System.out.println("Result Part 1 (26): " + resultPart1);
            assert resultPart1 == 26;
        } else {
            System.out.println("Result Part 1 (5125700): " + resultPart1);
            assert resultPart1 == 5125700;
        }



        coveredRegionPart = getCoveredRegion(probeAreaXPart2, probeAreaYPart2, sensorToBeacon);

        final Integer minX = coveredRegionPart.stream().map(Position::x).reduce(1, Integer::min);
        final Integer maxX = coveredRegionPart.stream().map(Position::x).reduce(1, Integer::max);
        final Integer minY = coveredRegionPart.stream().map(Position::y).reduce(1, Integer::min);
        final Integer maxY = coveredRegionPart.stream().map(Position::y).reduce(1, Integer::max);
        final Set<Position> rect = new HashSet<>();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                final Position e = new Position(x, y);
                if (!coveredRegionPart.contains(e)) {
                    rect.add(e);
                }
            }
        }


        final Position distressBeacon = rect.iterator().next();
        int resultPart2 =  distressBeacon.x * 4000000 + distressBeacon.y;
        if (example) {
            System.out.println("Result Part 2 (56000011): " + resultPart2);
            assert resultPart2 == 56000011;
        } else {
            System.out.println("Result Part 1 (): " + resultPart2);
            assert resultPart2 == 5125700;
        }

    }

    private static Set<Position> getCoveredRegion(Range<Integer> probeAreaX, Range<Integer> probeAreaY, Map<Position, Position> sensorToBeacon) {
        final Set<Position> coveredRegion = new HashSet<>();

        int n=0;
        for (Map.Entry<Position, Position> entry : sensorToBeacon.entrySet()) {
            Position S = entry.getKey();
            Position B = entry.getValue();

            int distance = S.distance(B);

            Set<Position> area = generatePositionsByArea(S, distance, probeAreaX, probeAreaY);
            System.out.println(n++);
            coveredRegion.addAll(area);
        }

        return coveredRegion;
    }

    private static Map<Position, Position> parseSensorsAndBeacons(List<String> stringInput) {
        final Map<Position, Position> sensorToBeacon = new HashMap<>();

        final Set<Position> sensors = new HashSet<>();
        final Set<Position> beacons = new HashSet<>();

        final Pattern pattern = Pattern.compile("Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)");
        for (String line : stringInput) {
            final Matcher matcher = pattern.matcher(line);

            matcher.find();

            Position S = new Position(
                    Integer.parseInt(matcher.group(1)),
                    Integer.parseInt(matcher.group(2))
            );
            sensors.add(S);

            Position B = new Position(
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4))
            );
            beacons.add(B);

            sensorToBeacon.put(S, B);
        }

        return sensorToBeacon;
    }

    private static Set<Position> generatePositionsByArea(Position p, int distance, Range<Integer> xRange, Range<Integer> yRange) {
        final Set<Position> areaAroundP = new HashSet<>();

        final int y0 = p.y - distance;
        final int yn = p.y + distance;

        for (int y = y0; y <= yn; y++) {
            if (yRange.contains(y)) {

                int x0 = p.x - (distance - abs(y - p.y));
                int xn = p.x + (distance - abs(y - p.y));
                if (xRange.hasLowerBound() && xRange.hasUpperBound()) {
                    x0 = max(x0, xRange.lowerEndpoint());
                    xn = min(xn, xRange.upperEndpoint());
                }

                for (int x = x0; x <= xn; x++) {
                    if (xRange.contains(x)) {
                        final Position candidate = new Position(x, y);
                        if (p.distance(candidate) <= distance) {
                            areaAroundP.add(candidate);
                        }
                    }
                }
            }
        }

        return areaAroundP;
    }

    private record Position(int x, int y) {
        int distance(Position that) {
            return abs(this.x - that.x) + abs(this.y - that.y);
        }
    }

}
