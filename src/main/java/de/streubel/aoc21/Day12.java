package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;


public class Day12 extends AdventOfCodeRunner {

    private static int visitMode;

    @Override
    public void run(List<String> stringInput) {

        Set<Cave> caves = parseCaves(stringInput);

        Cave start = caves.stream().filter(cave -> cave.name.equals("start")).findAny().get();

        List<Path> paths;

        visitMode = 1;
        paths = visit(start, new Path());
        System.out.println("Result Part 1: "+paths.size());

        visitMode = 2;
        paths = visit(start, new Path());
        System.out.println("Result Part 2: "+paths.size());
    }

    private static Set<Cave> parseCaves(List<String> stringInput) {
        Set<Cave> caves = new HashSet<>();

        for (String connection : stringInput) {
            final String[] caveNames = connection.split("-");

            Cave left = caves.stream()
                    .filter(cave -> cave.name.equals(caveNames[0]))
                    .findFirst()
                    .orElse(null);

            if (left == null) {
                left = new Cave(caveNames[0]);
                caves.add(left);
            }

            Cave right = caves.stream()
                    .filter(cave -> cave.name.equals(caveNames[1]))
                    .findFirst()
                    .orElse(null);

            if (right == null) {
                right = new Cave(caveNames[1]);
                caves.add(right);
            }

            left.connect(right);
        }
        return caves;
    }

    private static List<Path> visit(Cave cave, Path path) {

        if (path.canNotBeVisited(cave)) {
            return null;
        }

        List<Path> paths = new ArrayList<>();

        path.add(cave);

        if (cave.name.equals("end")) {
            paths.add(path);
        } else {
            for (Cave neighbour : cave.connectedCaves) {
                Path branch = new Path();
                branch.add(path);

                final List<Path> visit = visit(neighbour, branch);
                if (visit != null) {
                    paths.addAll(visit);
                }
            }
        }

        return paths;
    }

    private static class Cave {
        String name;
        Set<Cave> connectedCaves;

        public Cave(String name) {
            this.name = name;
            this.connectedCaves = new HashSet<>();
        }

        public void connect(Cave neighbour) {
            connectedCaves.add(neighbour);
            neighbour.connectedCaves.add(this);
        }

        public boolean isBig() {
            return name.matches("[A-Z]{1,2}");
        }

        public boolean isSmall() {
            return name.matches("[a-z]{1,2}");
        }
    }

    private static class Path {
        List<Cave> path;

        public Path() {
            this.path = new ArrayList<>();
        }

        public void add(Cave cave) {
            path.add(cave);
        }

        public void add(Path other) {
            path.addAll(other.path);
        }

        public boolean canNotBeVisited(Cave cave) {
            final long count = path.stream().filter(c -> c.name.equals(cave.name)).count();

            boolean notAllowed;
            if (cave.isBig()) {
                notAllowed = false;
            } else if (cave.isSmall()) {
                if (visitMode == 1) {
                    notAllowed = count >= 1;
                } else {
                    final Long x = path.stream()
                            .filter(Cave::isSmall)
                            .collect(groupingBy(identity(), counting()))
                            .values()
                            .stream()
                            .max(Long::compareTo)
                            .orElse(0L);
                    notAllowed = x >= 2 && count >= 1;
                }
            } else {
                notAllowed = count >= 1;
            }

            return notAllowed;
        }
    }
}
