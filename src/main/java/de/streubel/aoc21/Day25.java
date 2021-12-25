package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.List;


public class Day25 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final char[][] seafloor = stringInput.stream().map(String::toCharArray).toArray(char[][]::new);

        int n=1;
        while (canBeMoved(seafloor)) {
            move(seafloor);
            n++;
        }

        System.out.println("Result Part 1: "+n);
    }

    private static void move(char[][] seafloor) {

        char[][] copy = new char[seafloor.length][seafloor[0].length];

        for (int row = 0; row < seafloor.length; row++) {
            System.arraycopy(seafloor[row], 0, copy[row], 0, seafloor[row].length);
        }

        // east
        for (int row = 0; row < seafloor.length; row++) {
            for (int col = 0; col < seafloor[row].length; col++) {
                final int nextCol = (col + 1) % seafloor[row].length;
                if (seafloor[row][col] == '>' &&
                    seafloor[row][nextCol] == '.') {
                    copy[row][nextCol] = '>';
                    copy[row][col] = '.';
                }
            }
        }

        for (int row = 0; row < seafloor.length; row++) {
            System.arraycopy(copy[row], 0, seafloor[row], 0, seafloor[row].length);
        }

        //south
        for (int row = 0; row < seafloor.length; row++) {
            for (int col = 0; col < seafloor[row].length; col++) {
                final int nextRow = (row + 1) % seafloor.length;
                if (seafloor[row][col] == 'v' &&
                    seafloor[nextRow][col] == '.') {
                    copy[nextRow][col] = 'v';
                    copy[row][col] = '.';
                }
            }
        }

        System.arraycopy(copy, 0, seafloor, 0, copy.length);
    }

    private static boolean canBeMoved(char[][] seafloor) {

        for (int row = 0; row < seafloor.length; row++) {
            for (int col = 0; col < seafloor[row].length; col++) {
                int nextRow = (row + 1) % seafloor.length;
                int nextCol = (col + 1) % seafloor[row].length;
                if (seafloor[row][col] == '>' && seafloor[row][nextCol] == '.') {
                    return true;
                }
                if (seafloor[row][col] == 'v' && seafloor[nextRow][col] == '.') {
                    return true;
                }
            }
        }

        return false;
    }
}
