package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.abs;

public class Day09 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final Set<Location> pathKnot1 = new HashSet<>();
        final Set<Location> pathKnot9 = new HashSet<>();

        Location h = new Location(0, 0);
        final Location[] knots = new Location[9];
        for (int i = 0; i < knots.length; i++) {
            knots[i] = new Location(0, 0);
        }

        pathKnot1.add(knots[0]);
        pathKnot9.add(knots[8]);

        for (String line : stringInput) {
            final String[] s = line.split(" ");
            final String direction = s[0];
            final int steps = Integer.parseInt(s[1]);

            for (int i = 0; i < steps; i++) {
                h = h.move1Step(direction);

                knots[0] = knots[0].follow(h);
                for (int k = 1; k < knots.length; k++) {
                    knots[k] = knots[k].follow(knots[k-1]);
                }

                pathKnot1.add(knots[0]);
                pathKnot9.add(knots[8]);
            }
        }

        int resultPart1 = pathKnot1.size();
        System.out.println("Result Part 1 (6339): " + resultPart1);
        assert resultPart1 == 6339;

        int resultPart2 = pathKnot9.size();
        System.out.println("Result Part 2 (2541): " + resultPart2);
        assert resultPart2 == 2541;
    }


    private record Location(int x, int y) {

        public Location move1Step(String direction) {
            return switch (direction) {
                case "R" -> new Location(x + 1, y);
                case "L" -> new Location(x - 1, y);
                case "U" -> new Location(x, y + 1);
                case "D" -> new Location(x, y - 1);
                default -> throw new IllegalStateException("Unexpected value: " + direction);
            };
        }

        public int distanceTo(Location h) {
            return (x - h.x)*(x - h.x) + (y - h.y)*(y - h.y);
        }
        
        public boolean isAdjacent(Location h) {
            return distanceTo(h) <= 2;
        }

        public Location follow(Location h) {
            Location newLoc = null;
            if (this.isAdjacent(h)) {
                newLoc = this;
            } else {
                if (distanceTo(h) == 4 || distanceTo(h) == 8) {
                    newLoc = new Location(x+(h.x-x)/2, y+(h.y - y)/2);
                }

                else if (distanceTo(h) == 5) {
                    if (abs(h.x - x) == 1 && abs(h.y - y) == 2) {
                        newLoc = new Location(x+(h.x-x), y+(h.y - y)/2);
                    } else if (abs(h.x - x) == 2 && abs(h.y - y) == 1) {
                        newLoc = new Location(x+(h.x-x)/2, y+(h.y - y));
                    }
                }
            }

            return newLoc;
        }

        @Override
        public String toString() {
            return "[" + x + ", " + y + ']';
        }
    }
}
