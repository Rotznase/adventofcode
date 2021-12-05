package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day05 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {


        List<Vent> vents = stringInput.stream()
                .map(Vent::new)
                .filter(v -> v.isHorizontal() || v.isVertical())
                .collect(Collectors.toList());

        Map<Point, Integer> map = createMap(vents);

        long count = map.values().stream().filter(nr -> nr >= 2).count();
        System.out.println("Result Part 1: "+count);


        vents = stringInput.stream()
                .map(Vent::new)
                .filter(v -> v.isHorizontal() || v.isVertical() || v.isDiagonal())
                .collect(Collectors.toList());

        map = createMap(vents);

        count = map.values().stream().filter(nr -> nr >= 2).count();
        System.out.println("Result Part 2: "+count);
    }

    private Map<Point, Integer> createMap(List<Vent> vents) {
        Map<Point, Integer> map = new HashMap<>();
        for (Vent vent : vents) {
            for (Point p : vent.getLine()) {
                map.put(p, map.getOrDefault(p, 0)+1);
            }
        }

        return map;
    }

    private static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || o.getClass() != Point.class) {
                return false;
            }

            final Point that = (Point) o;
            return this.x == that.x && this.y == that.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    private static class Vent {
        Point _1;
        Point _2;

        public Vent(String s) {
            final Pattern pattern = Pattern.compile("(\\d+),(\\d+) -> (\\d+),(\\d+)");
            final Matcher matcher = pattern.matcher(s.trim());

            if (matcher.matches()) {
                this._1 = new Point(
                        Integer.parseInt(matcher.group(1)),
                        Integer.parseInt(matcher.group(2))
                );
                this._2 = new Point(
                        Integer.parseInt(matcher.group(3)),
                        Integer.parseInt(matcher.group(4))
                );
            }
        }

        public boolean isHorizontal() {
            return _1.y == _2.y;
        }

        public boolean isVertical() {
            return _1.x ==_2.x;
        }

        public boolean isDiagonal() {
              return Math.abs(_1.x - _2.x) == Math.abs(_1.y - _2.y);
        }

        public List<Point> getLine() {
            List<Point> graph = new ArrayList<>();
            if (isHorizontal()) {
                int min = Math.min(_1.x, _2.x);
                int max = Math.max(_1.x, _2.x);
                for (int x = min; x <= max; x++) {
                    graph.add(new Point(x, _1.y));
                }
            } else if (isVertical()) {
                int min = Math.min(_1.y, _2.y);
                int max = Math.max(_1.y, _2.y);
                for (int y = min; y <= max; y++) {
                    graph.add(new Point(_1.x, y));
                }
            } else if (isDiagonal()) {
                int min_x = Math.min(_1.x, _2.x);
                int max_x = Math.max(_1.x, _2.x);
                int sign_x = _1.x < _2.x ? +1 : -1;
                int sign_y = _1.y < _2.y ? +1 : -1;

                for (int i=0; i<=max_x-min_x; i++) {
                    graph.add(new Point(_1.x+sign_x*i, _1.y+sign_y*i));
                }
            }

            return graph;
        }
    }                                               
}
