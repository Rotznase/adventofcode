package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;


public class Day13 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final int DOT_DEFINITION = 0;
        final int FOLD_INSTRACTIONS = 1;

        Set<Coords> dotList = new HashSet<>();
        List<Coords> folding = new ArrayList<>();

        int intputPart = DOT_DEFINITION;

        for (String line : stringInput) {
            if (line.isEmpty()) {
                intputPart = FOLD_INSTRACTIONS;
            }

            switch (intputPart) {
                case DOT_DEFINITION:
                    final String[] coordString = line.split(",");
                    dotList.add(new Coords(parseInt(coordString[0]), parseInt(coordString[1])));
                    break;

                case FOLD_INSTRACTIONS:
                    Pattern pattern = Pattern.compile("fold along ([xy])=(\\d+)");
                    final Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        switch (matcher.group(1)) {
                            case "x": folding.add(new Coords(parseInt(matcher.group(2)), 0));
                                      break;
                            case "y": folding.add(new Coords(0, parseInt(matcher.group(2))));
                                      break;
                        }
                    }
                    break;
            }
        }


        Set<Coords> folded = fold(dotList, folding.get(0));

        int nrOfVisibleDots = folded.size();
        System.out.println("Result Part 1: "+nrOfVisibleDots);


        folded = new HashSet<>(dotList);
        for (Coords foldLine : folding) {
            folded = fold(folded, foldLine);
        }

        String display = display(folded);
        System.out.println("Result Part 2: \n"+display);
    }

    private static Set<Coords> fold(Set<Coords> dots, Coords foldline) {
        Set<Coords> newList = new HashSet<>();
        for (Coords coords : dots) {
            if ((foldline.x == 0 && coords.y > foldline.y) || (foldline.y == 0 && coords.x > foldline.x)) {
                newList.add(reflect(coords, foldline));
            } else {
                newList.add(coords);
            }
        }

        return newList;
    }

    public static Coords reflect(Coords coords, Coords foldline) {
        Coords folded;
        if (foldline.y == 0) {
            folded = new Coords(2*foldline.x - coords.x, coords.y);
        } else if (foldline.x == 0) {
            folded = new Coords(coords.x, 2*foldline.y - coords.y);
        } else {
            throw new IllegalArgumentException();
        }

        return folded;
    }

    public static String display(Set<Coords> dots) {
        int minX = dots.stream().mapToInt(Coords::getX).min().orElse(0);
        int maxX = dots.stream().mapToInt(Coords::getX).max().orElse(0);
        int minY = dots.stream().mapToInt(Coords::getY).min().orElse(0);
        int maxY = dots.stream().mapToInt(Coords::getY).max().orElse(0);

        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < maxY - minY+1; y++) {
            char[] line = new char[maxX-minX+1];
            for (int x = 0; x < maxX - minX+1; x++) {
                final Coords displayPos = new Coords(x, y);
                final Optional<Coords> first = dots.stream().filter(coords -> coords.equals(displayPos)).findFirst();
                if (first.isPresent()) {
                    line[x] = '#';
                } else {
                    line[x] = '.';
                }
            }
            sb.append(new String(line)).append("\n");
        }


        return sb.toString();
    }

    private static class Coords {
        int x;
        int y;

        public Coords(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coords coords = (Coords) o;
            return x == coords.x && y == coords.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }

        @Override
        public String toString() {
            return "" + x + "," + y;
        }
    }
}
