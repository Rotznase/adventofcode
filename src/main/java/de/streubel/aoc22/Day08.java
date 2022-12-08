package de.streubel.aoc22;

import com.google.common.primitives.Chars;
import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

public class Day08 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        char[][] grid = new char[stringInput.size()][];
        int n = 0;
        for (String line : stringInput) {
            grid[n++] = line.toCharArray();
        }

        final int NORTH = 0x1;
        final int EAST = 0x2;
        final int SOUTH = 0x4;
        final int WEST = 0x8;

        int[][] visibility = new int[grid.length][grid[0].length];
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (row == 0) {
                    visibility[row][col] |= NORTH;
                }
                if (row == grid.length-1) {
                    visibility[row][col] |= SOUTH;
                }
                if (col == 0) {
                    visibility[row][col] |= WEST;
                }
                if (col == grid[row].length-1) {
                    visibility[row][col] |= EAST;
                }
                if (visibility[row][col] == 0) {
                    char max;

                    max = 0;
                    for (int i = row - 1; i > -1; i--) {
                        max = max(max, grid[i][col]);
                    }
                    if (grid[row][col] > max) {
                        visibility[row][col] |= NORTH;
                    }


                    max = 0;
                    for (int i = row + 1; i < grid.length; i++) {
                        max = max(max, grid[i][col]);
                    }
                    if (grid[row][col] > max) {
                        visibility[row][col] |= SOUTH;
                    }

                    max = 0;
                    for (int i = col + 1; i < grid.length; i++) {
                        max = max(max, grid[row][i]);
                    }
                    if (grid[row][col] > max) {
                        visibility[row][col] |= WEST;
                    }

                    max = 0;
                    for (int i = col - 1; i > -1; i--) {
                        max = max(max, grid[row][i]);
                    }
                    if (grid[row][col] > max) {
                        visibility[row][col] |= EAST;
                    }
                }
            }
        }

        int resultPart1 = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (visibility[row][col] != 0) {
                    resultPart1++;
                }
            }
        }

        System.out.println("Result Part 1 (1807): " + resultPart1);


        int[][] scenicScore = new int[grid.length][grid[0].length];
        for (int row = 1; row < grid.length-1; row++) {
            for (int col = 1; col < grid[row].length-1; col++) {

                int scenicScoreNorth = 0;
                for (int i = row - 1; i > -1; i--) {
                    scenicScoreNorth++;
                    if (grid[row][col] <= grid[i][col]) {
                        break;
                    }
                }


                int scenicScoreSouth = 0;
                for (int i = row + 1; i < grid.length; i++) {
                    scenicScoreSouth++;
                    if (grid[row][col] <= grid[i][col]) {
                        break;
                    }
                }


                int scenicScoreWest = 0;
                for (int i = col + 1; i < grid.length; i++) {
                    scenicScoreWest++;
                    if (grid[row][col] <= grid[row][i]) {
                        break;
                    }
                }


                int scenicScoreEast = 0;
                for (int i = col - 1; i > -1; i--) {
                    scenicScoreEast++;
                    if (grid[row][col] <= grid[row][i]) {
                        break;
                    }
                }

                scenicScore[row][col] = scenicScoreNorth * scenicScoreSouth * scenicScoreWest * scenicScoreEast;
            }
        }

        int resultPart2 = 0;
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                resultPart2 = Math.max(resultPart2, scenicScore[row][col]);
            }
        }

        System.out.println("Result Part 2 (480000): " + resultPart2);

    }

    public static char max(char a, char b) {
        return (a >= b) ? a : b;
    }

}
