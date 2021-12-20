package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.List;


public class Day20 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        char[] imageEnhancementAlgorithm = stringInput.get(0).toCharArray();
        char[][] origImage = stringInput
                .subList(2, stringInput.size())
                .stream()
                .map(String::toCharArray)
                .toArray(char[][]::new);

        System.out.println("Result Part 1: "+ getNrOfLitPixel(imageEnhancementAlgorithm, origImage, 2));
        System.out.println("Result Part 2: "+ getNrOfLitPixel(imageEnhancementAlgorithm, origImage, 50));
    }

    private int getNrOfLitPixel(char[] imageEnhancementAlgorithm, char[][] origImage, int N) {

        int border = 2*N;

        char[][] currentImage = enhance(origImage.clone(), border);
        char[][] newImage = new char[currentImage.length][currentImage[0].length];

        for (int n = 0; n < N; n++) {
            for (int row = 0; row < currentImage.length; row++) {
                for (int col = 0; col < currentImage[row].length; col++) {
                    int pixelIndex = getPixelIndex(currentImage, row, col);
                    newImage[row][col] = imageEnhancementAlgorithm[pixelIndex];
                }
            }

            for (int row = 0; row < currentImage.length; row++) {
                System.arraycopy(newImage[row], 0, currentImage[row], 0, currentImage[row].length);
            }
        }

        int nrOfLitPixel = 0;
        for (int row = border-N; row < currentImage.length - (border-N); row++) {
            for (int col = border-N; col < currentImage[row].length - (border-N); col++) {
                if (currentImage[row][col] == '#') {
                    nrOfLitPixel++;
                }
            }
        }

        return nrOfLitPixel;
    }

    private char[][] enhance(char[][] grid, int border) {
        char[][] result = new char[grid.length+2*border][grid[0].length+2*border];

        for (int row = 0; row < result.length; row++) {
            for (int col = 0; col < result[row].length; col++) {
                if (row <= border-1 || row >= result.length-(border-1)-1 ||
                    col <= border-1 || col >= result[row].length-(border-1)-1) {
                    result[row][col] = '.';
                } else {
                    result[row][col] = grid[row-border][col-border];
                }
            }
        }

        return result;
    }
    
    private static int getPixelIndex(char[][] totalGrid, int centerRow, int centerCol) {
        StringBuilder sb = new StringBuilder();
        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                if (centerRow + row >= 0 && centerRow + row < totalGrid.length &&
                    centerCol + col >= 0 && centerCol + col < totalGrid.length
                ) {
                    switch (totalGrid[centerRow + row][centerCol + col]) {
                        case '.': sb.append(0); break;
                        case '#': sb.append(1); break;
                    }
                } else {
                    sb.append('0');
                }
            }
        }

        return Integer.parseInt(sb.toString(), 2);
    }
}
