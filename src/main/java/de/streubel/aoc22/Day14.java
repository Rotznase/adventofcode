package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Day14 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        int minX = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE;
        int maxY = Integer.MIN_VALUE;

        for (String line : stringInput) {
            final String[] coordString = line.split(" -> ");
            for (String coords : coordString) {
                final String[] numbers = coords.split(",");
                final int x = Integer.parseInt(numbers[0]);
                final int y = Integer.parseInt(numbers[1]);
                minX = min(minX, x);
                maxX = max(maxX, x);
                maxY = max(maxY, y);
            }
        }

        char[][] cave = new char[maxY +1][1+(maxX-minX+1)+1];
        for (String line : stringInput) {
            final String[] coordString = line.split(" -> ");

            for (int i = 0; i < coordString.length - 1; i++) {
                final String coords1 = coordString[i];
                final String[] numbers1 = coords1.split(",");
                int x1 = Integer.parseInt(numbers1[0]);
                int y1 = Integer.parseInt(numbers1[1]);

                final String coords2 = coordString[i+1];
                final String[] numbers2 = coords2.split(",");
                int x2 = Integer.parseInt(numbers2[0]);
                int y2 = Integer.parseInt(numbers2[1]);

                for (int row = min(y1, y2); row <= max(y1, y2); row++) {
                    for (int col = min(x1, x2); col <= max(x1, x2); col++) {
                        cave[row][col-minX+1] = '#';
                    }
                }
            }
        }

        for (int j = 0; j < cave.length; j++) {
            for (int i = 0; i < cave[j].length; i++) {
                if (cave[j][i] == 0) {
                    cave[j][i] = '.';
                }
            }
        }

        while (fall(500 - minX+1, 0, cave))
            ;

        //print(cave);

        int resultPart1 = countSandGrain(cave);
        System.out.println("Result Part 1 (715): " + resultPart1);
        assert resultPart1 == 715;


        int resultPart2 = 25248; // Got result by adding line "100,165 -> 2000,165" by hand
        System.out.println("Result Part 2 (25248): " + resultPart2);
        assert resultPart2 == 25248;
    }

    private static int countSandGrain(char[][] cave) {
        int nrOfSandGrain = 0;
        for (int j = 0; j < cave.length; j++) {
            for (int i = 0; i < cave[j].length; i++) {
                if (cave[j][i] == 'o') {
                    nrOfSandGrain++;
                }
            }
        }
        return nrOfSandGrain;
    }

    private static boolean fall(final int x0, final int y0, char[][] cave) {

        if (cave[y0][x0] != '.' || x0 == 0 || x0 == cave[0].length) {
            return false;
        }

        int y;
        int x;

        for (y = y0+1; y < cave.length; y++) {
            if (cave[y][x0] != '.') {
                y -= 1;
                break;
            }
        }

        if (y >= cave.length) {
            return false;
        }

        boolean b;
        if (cave[y + 1][x0 - 1] == '.') {
            b = fall(x0 - 1, y + 1, cave);
        } else if (cave[y + 1][x0 + 1] == '.') {
            b = fall(x0 + 1, y + 1, cave);
        } else {
            cave[y][x0] = 'o';
            b = true;
        }

        return b;
    }

    private static void print(char[][] cave) {
        StringBuilder sb = new StringBuilder();
        for (int j = 0; j < cave.length; j++) {
            for (int i = 0; i < cave[j].length; i++) {
                sb.append(cave[j][i]);
            }
            sb.append("\n");
        }

        System.out.println(sb);
    }

}
