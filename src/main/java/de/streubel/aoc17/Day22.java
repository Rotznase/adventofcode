package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day22 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        Map<Long, Node> nodes = parseInput(gridComputingCluster);

        Carrier carrier = new Carrier1(gridComputingCluster[0].length()/2, gridComputingCluster.length/2, Direction.NORTH);

        for (int i=0; i<10000; i++) {
            carrier.burst(nodes);
        }

        System.out.println("Part 1: "+carrier.infectionCounter);

        nodes = parseInput(gridComputingCluster);

        carrier = new Carrier2(gridComputingCluster[0].length()/2, gridComputingCluster.length/2, Direction.NORTH);

        for (int i=0; i<10000000; i++) {
            carrier.burst(nodes);
        }

        System.out.println("Part 2: "+carrier.infectionCounter);
    }

    class Carrier1 extends Carrier {
        Carrier1(int x, int y, Direction dir) {
            super(x, y, dir);
        }

        void process(Node node) {
            switch (node.state) {
                case INFECTED:
                    dir = dir.rotateRight();
                    node.makeClean();
                    break;
                case CLEAN:
                    dir = dir.rotateLeft();
                    node.makeInfected();
                    infectionCounter++;
                    break;
            }
        }
    }

    class Carrier2 extends Carrier {
        Carrier2(int x, int y, Direction dir) {
            super(x, y, dir);
        }

        void process(Node node) {
            switch (node.state) {
                case CLEAN:
                    dir = dir.rotateLeft();
                    node.makeWeakened();
                    break;
                case WEAKENED:
                    // carrier is heading in the same direction
                    node.makeInfected();
                    infectionCounter++;
                    break;
                case INFECTED:
                    dir = dir.rotateRight();
                    node.makeFlagged();
                    break;
                case FLAGGED:
                    dir = dir.reverse();
                    node.makeClean();
                    break;
            }
        }
    }

    abstract class Carrier {
        int x;
        int y;
        Direction dir;

        int infectionCounter;

        Carrier(int x, int y, Direction dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.infectionCounter = 0;
        }

        void burst(Map<Long, Node> nodes) {
            final Node node = nodes.getOrDefault(keyOf(x,y),
                    new Node(x, y, InfectionState.CLEAN));

            if (!nodes.containsKey(keyOf(x,y))) {
                nodes.put(keyOf(x,y), node);
            }

            process(node);

            if (node.state.isClean()) {
                nodes.remove(node);
            }

            move();
        }

        abstract void process(Node node);

        void move() {
            switch (dir) {
                case NORTH: y--;break;
                case SOUTH: y++;break;
                case WEST:  x--;break;
                case EAST:  x++;break;
            }
        }
    }

    class Node {
        int x;
        int y;
        InfectionState state;

        Node(int x, int y, InfectionState infectionState) {
            this.x = x;
            this.y = y;
            this.state = infectionState;
        }

        void makeClean() {
            state = InfectionState.CLEAN;
        }

        void makeWeakened() {
            state = InfectionState.WEAKENED;
        }

        void makeInfected() {
            state = InfectionState.INFECTED;
        }

        void makeFlagged() {
            state = InfectionState.FLAGGED;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            if (x != node.x) return false;
            return y == node.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    enum InfectionState {
        CLEAN, WEAKENED, INFECTED, FLAGGED;

        boolean isClean() {
            return this == CLEAN;
        }

        boolean isWeakened() {
            return this == WEAKENED;
        }

        boolean isInfected() {
            return this == INFECTED;
        }

        boolean isFlagged() {
            return this == FLAGGED;
        }

        public InfectionState nextState() {
            switch (this) {
                case CLEAN:     return WEAKENED;
                case WEAKENED:  return INFECTED;
                case INFECTED:  return FLAGGED;
                case FLAGGED:   return CLEAN;
            }
            return null;
        }
    }

    enum Direction {
        NORTH, SOUTH, WEST, EAST;

        Direction rotateLeft() {
            switch (this) {
                case NORTH: return WEST;
                case WEST:  return SOUTH;
                case SOUTH: return EAST;
                case EAST:  return NORTH;
            }
            return null;
        }

        Direction rotateRight() {
            switch (this) {
                case NORTH: return EAST;
                case WEST:  return NORTH;
                case SOUTH: return WEST;
                case EAST:  return SOUTH;
            }
            return null;
        }

        Direction reverse() {
            switch (this) {
                case NORTH: return SOUTH;
                case WEST:  return EAST;
                case SOUTH: return NORTH;
                case EAST:  return WEST;
            }
            return null;
        }
    }

    private EnumSet<InfectionState> allowedeStates = EnumSet.of(InfectionState.CLEAN, InfectionState.INFECTED);

    Map<Long, Node> parseInput(String[] input) {
        Map<Long, Node> infectedNodes = new HashMap<>();
        for (int y=0; y<input.length; y++) {
            for (int x=0; x<input[y].length(); x++) {
                if (input[y].charAt(x) == '#') {
                    infectedNodes.put(keyOf(x,y), new Node(x, y, InfectionState.INFECTED));
                }
            }
        }

        return infectedNodes;
    }

    long keyOf(int x, int y) {
        return x+y*1000000;
    }
    private String[] gridComputingCluster = {
//        ".........",
//        ".........",
//        ".........",
//        ".....#...",
//        "...#.....",
//        ".........",
//        ".........",
//        ".........",
        "#.#.#.##.#.##.###.#.###.#",
        ".#..#.....#..#######.##.#",
        "......###..##..###..#...#",
        "##....#.#.#....#..#..#..#",
        "#..#....#.##.#.#..#..#.#.",
        "..##..##.##..##...#...###",
        "..#.#....#..####.##.##...",
        "###...#.#...#.######...#.",
        "..#####...###..#####.#.##",
        "...#..#......####.##..#.#",
        "#...##..#.#####...#.##...",
        "..#.#.###.##.##....##.###",
        "##.##...###....#######.#.",
        "#.#...#.#..#.##..##..##.#",
        ".#...###...#..#..####....",
        "####...#...##.####..#.#..",
        "......#.....##.#.##....##",
        "###.......####..##.#.##..",
        "....###.....##.##..###.#.",
        ".##..##.#.###.###..#.###.",
        "..#..##.######.##........",
        "#..#.#..#.###....##.##..#",
        ".##.#.#...######...##.##.",
        "##..#..#..##.#.#..#..####",
        "#######.#.######.#.....##",
    };
}
