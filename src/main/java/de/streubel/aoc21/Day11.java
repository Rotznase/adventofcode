package de.streubel.aoc21;

import com.google.common.collect.Range;
import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.List;


public class Day11 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        List<Octopus> octos;

        octos = parseInput(stringInput);
        for (int step = 1; step <= 100; step++) {
            octos.forEach(Octopus::increaseEnergy);
            octos.forEach(Octopus::unlock);
        }

        long totalNrOfFlashes = octos.stream().mapToInt(Octopus::getNrOfFlashes).sum();
        System.out.println("Result Part 1: "+totalNrOfFlashes);


        octos = parseInput(stringInput);
        int stepWith100Flashes = 1;
        while (true) {
            octos.forEach(Octopus::increaseEnergy);
            octos.forEach(Octopus::unlock);
            totalNrOfFlashes = octos.stream().filter(octo -> octo.getEngeryLevel() == 0).count();
            if (totalNrOfFlashes == 100) {
                break;
            }
            stepWith100Flashes++;
        }
        
        System.out.println("Result Part 2: "+stepWith100Flashes);
    }

    private List<Octopus> parseInput(List<String> stringInput) {
        List<Octopus> octos = new ArrayList<>();

        Octopus[][] octoGrid = new Octopus[stringInput.size()][];
        for (int row = 0; row < octoGrid.length; row++) {
            octoGrid[row] = new Octopus[stringInput.get(row).length()];
            for (int col = 0; col < octoGrid.length; col++) {
                final int engergyLevel = Character.digit(stringInput.get(row).toCharArray()[col], 10);
                octoGrid[row][col] = new Octopus(engergyLevel);
                octos.add(octoGrid[row][col]);
            }
        }

        Range<Integer> range = Range.closed(0,9);
        for (int row = 0; row < octoGrid.length; row++) {
            for (int col = 0; col < octoGrid.length; col++) {
                for (int row_neigbor = row-1; row_neigbor <= row+1; row_neigbor++) {
                    for (int col_neigbor = col-1; col_neigbor <= col+1; col_neigbor++) {
                        if (range.contains(row_neigbor) && range.contains(col_neigbor)) {
                            octoGrid[row][col].addAdjacentOctos(octoGrid[row_neigbor][col_neigbor]);
                        }
                    }
                }
            }
        }

        return octos;
    }


    private static class Octopus {
        int engeryLevel;
        List<Octopus> adjacentOctos;
        boolean locked;
        int nrOfFlashes;

        public Octopus(int engeryLevel) {
            this.engeryLevel = engeryLevel;
            adjacentOctos = new ArrayList<>();
            locked = false;
            nrOfFlashes = 0;
        }

        public int getEngeryLevel() {
            return engeryLevel;
        }

        public void addAdjacentOctos(Octopus neighbor) {
            adjacentOctos.add(neighbor);
        }

        public void increaseEnergy() {
            if (!locked) {
                locked = true;
                engeryLevel++;
                if (engeryLevel > 9) {
                    adjacentOctos.forEach(Octopus::increaseEnergy);
                    engeryLevel = 0;
                    nrOfFlashes++;
                } else {
                    locked = false;
                }
            }
        }

        public void unlock() {
            locked = false;
        }

        public int getNrOfFlashes() {
            return nrOfFlashes;
        }
    }

//    private static void printGrid(Octopus[][] octoGrid) {
//        for (int row = 0; row < octoGrid.length; row++) {
//            for (int col = 0; col < octoGrid.length; col++) {
//                System.out.print(octoGrid[row][col].engeryLevel);
//            }
//            System.out.println();
//        }
//        System.out.println();
//        System.out.println();
//    }
}
