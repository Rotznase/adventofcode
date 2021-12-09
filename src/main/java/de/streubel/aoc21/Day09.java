package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.max;

public class Day09 extends AdventOfCodeRunner {


    @Override
    public void run(List<String> stringInput) {

        final int[][] heightmap = new int[stringInput.size()][];

        for (int row = 0; row < stringInput.size(); row++) {
            final char[] line = stringInput.get(row).toCharArray();
            heightmap[row] = new int[line.length];
            for (int col = 0; col < line.length; col++) {
                heightmap[row][col] = Character.digit(line[col], 10);
            }
        }

        List<Integer> lowestPoints = new ArrayList<>();

        for (int row = 0; row < heightmap.length; row++) {
            for (int col = 0; col < heightmap[row].length; col++) {
                if (isLowest(heightmap, row, col)) {
                    lowestPoints.add(heightmap[row][col]);
                }
            }
        }

        final int sumOfRisk = lowestPoints.stream().mapToInt(h -> 1 + h).sum();

        System.out.println("Result Part 1: "+sumOfRisk);




        List<Region> regions = new ArrayList<>();

        for (int row = 0; row < heightmap.length; row++) {
            for (int col = 0; col < heightmap[row].length; col++) {

                if (heightmap[row][col] == 9) {
                    continue;
                }

                int _row = row;
                int _col = col;

                Region region = regions.stream()
                        .filter(r -> r.contains(_row, _col))
                        .findFirst()
                        .orElse(null);

                if (region != null) {
                    region.increaseEnclosed();
                } else {
                    region = new Region();
                    final Region _region = region;

                    region.addCoords(heightmap, row, col);
                    region.increaseEnclosed();

                    final List<Region> regsToMerge = regions.stream()
                            .filter(r -> r.overlaps(_region))
                            .collect(Collectors.toList());

                    region.merge(regsToMerge);

                    regions.removeAll(regsToMerge);
                    regions.add(region);
                }

            }
        }

        final int[] collect = regions.stream()
                .sorted(Comparator.comparingInt(o -> o.enclosed))
                .skip(max(regions.size() - 3, 0))
                .mapToInt(Region::getEnclosed)
                .toArray();

        int resultPart2 = 1;
        for (Integer size : collect) {
            resultPart2 *= size;
        }

        System.out.println("Result Part 2: "+resultPart2);
    }

    private static boolean isLowest(int[][] array, int row, int col) {

        int x = array[row][col];
        int north = row-1 >= 0 ? array[row-1][col] : Integer.MAX_VALUE;
        int south = row+1 < array.length ? array[row+1][col] : Integer.MAX_VALUE;
        int west  = col-1 >= 0 ? array[row][col-1] : Integer.MAX_VALUE;
        int east  = col+1 < array[row].length ? array[row][col+1] : Integer.MAX_VALUE;

        return x < north && x < south && x < west && x < east;
    }

    private static class Coords {
        int row;
        int col;

        public Coords(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Coords coords = (Coords) o;
            return row == coords.row && col == coords.col;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    }

    private static class Region {
        Set<Coords> coords;
        int enclosed;

        public Region() {
            this.coords = new HashSet<>();
            this.enclosed = 0;
        }

        public boolean contains(int row, int col) {
            return coords.stream().anyMatch(coord -> coord.equals(new Coords(row, col)));
        }

        public void increaseEnclosed() {
            enclosed++;
        }

        public int getEnclosed() {
            return enclosed;
        }

        public boolean overlaps(Region other) {
            return !Collections.disjoint(this.coords, other.coords);
        }

        public void addCoords(int[][] heightmap, int row, int col) {
            // north
            for (int r = row; r >= 0; r--) {
                if (heightmap[r][col] == 9) {
                    break;
                }
                coords.add(new Coords(r, col));
            }

            // south
            for (int r = row+1; r < heightmap.length; r++) {
                if (heightmap[r][col] == 9) {
                    break;
                }
                coords.add(new Coords(r, col));
            }

            // west
            for (int c = col; c >= 0; c--) {
                if (heightmap[row][c] == 9) {
                    break;
                }
                coords.add(new Coords(row, c));
            }

            // east
            for (int c = col+1; c < heightmap[row].length; c++) {
                if (heightmap[row][c] == 9) {
                    break;
                }
                coords.add(new Coords(row, c));
            }

        }

        public void merge(Collection<Region> regionsToMerge) {
            for (Region otherRegion : regionsToMerge) {
                this.coords.addAll(otherRegion.coords);
                this.enclosed += otherRegion.enclosed;
            }
        }
    }

}
