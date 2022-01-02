package de.streubel.aoc21;

import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Sets;
import de.streubel.AdventOfCodeRunner;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.TreeBidiMap;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.linear.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.round;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;


public class Day19 extends AdventOfCodeRunner {

    private static final int X = 0;
    private static final int Y = 1;
    private static final int Z = 2;

    private static final Comparator<int[]> comparator = (o1, o2) -> ComparisonChain
            .start()
            .compare(o1[X], o2[X])
            .compare(o1[Y], o2[Y])
            .compare(o1[Z], o2[Z])
            .result();

    @Override
    public void run(List<String> stringInput) {


        /*
         * Solution strategy:
         * We are looking for a transformation including a rotation and a translation.
         * In 3D the rotation consists of (unknown) 3x3 coefficients and the translation
         * of 1x3 coefficients. Altogether we are looking for 12 unknown numbers.
         * These coefficients can be solved by 12 equations which we get with 12 beacon
         * coordinates.
         */

        List<Scanner> scanners = new ArrayList<>();
        for (String line : stringInput) {
            if (line.indexOf("--- scanner ") == 0) {
                Pattern pattern = Pattern.compile("--- (scanner \\d+) ---");
                final Matcher matcher = pattern.matcher(line);
                if (matcher.matches()) {
                    scanners.add(new Scanner(matcher.group(1)));
                }
            } else if (!line.isEmpty()) {
                int [] xyz = Arrays.stream(line.split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                scanners.get(scanners.size()-1).addBeacon(xyz);
            }
        }


        scanners.forEach(Scanner::calculateDistances);


        Scanner rootScanner = scanners.get(0);
        rootScanner.fetchAdjacentScanners(new ArrayList<>(scanners.subList(1, scanners.size())));

        Set<int[]> overlappingBeacons = rootScanner.getOverlappingBeacons();

        System.out.println("Result Part 1: "+overlappingBeacons.size());

        int maxManhattanDistance = 0;
        for (int i = 0; i < scanners.size(); i++) {
            for (int j = i+1; j < scanners.size(); j++) {
                final int[] originScanner_i = rootScanner.getOrigin(scanners.get(i));
                final int[] originScanner_j = rootScanner.getOrigin(scanners.get(j));
                int distance = manhattanDistance(originScanner_i, originScanner_j);
                maxManhattanDistance = Math.max(maxManhattanDistance, distance);
            }
        }

        System.out.println("Result Part 2: "+maxManhattanDistance);

    }

    private static class Scanner {
        String name;
        List<int[]> beacons;
        BidiMap<Pair<int[], int[]>, Integer> distList;

        List<Scanner> adjacentScanners;
        Map<String, Transformation> trafos;

        public Scanner(String name) {
            this.name = name;
            this.beacons = new ArrayList<>();
            this.distList = new TreeBidiMap<>();
            this.adjacentScanners = new ArrayList<>();
            this.trafos = new HashMap<>();
        }

        public void addBeacon(int[] beacon) {
            beacons.add(beacon);
        }

        public void calculateDistances() {
            for (int i = 0; i < beacons.size(); i++) {
                for (int j = i + 1; j < beacons.size(); j++) {
                    int[] beacon1 = beacons.get(i);
                    int[] beacon2 = beacons.get(j);
                    distList.put(Pair.of(beacon1, beacon2), distance(beacon1, beacon2));
                }
            }
        }

        public Set<Integer> getDistances() {
            return distList.inverseBidiMap().keySet();
        }


        public Set<int[]> getDedicatedBeacons(Set<Integer> distances) {
            return distList.entrySet()
                    .stream()
                    .filter(entry -> distances.contains(entry.getValue()))
                    .sorted(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
                    .collect(Collectors.toSet());
        }

        public boolean isAdjacentTo(Scanner possiblyAdjacentScanner) {
            Set<Integer> distancesA = this.getDistances();
            Set<Integer> distancesB = possiblyAdjacentScanner.getDistances();
            Set<Integer> commonDistances = Sets.intersection(distancesA, distancesB);

            return commonDistances.size() == 66;
        }

        public void addAdjacentScanner(Scanner adjacentScanner) {
            this.adjacentScanners.add(adjacentScanner);
        }

        public void fetchAdjacentScanners(List<Scanner> possiblyAdjacentScanners) {
            List<Scanner> remainingScanners = new ArrayList<>(possiblyAdjacentScanners);
            for (Scanner possibleAdjacent : possiblyAdjacentScanners) {
                if (this.isAdjacentTo(possibleAdjacent)) {
                    this.addAdjacentScanner(possibleAdjacent);
                    remainingScanners.remove(possibleAdjacent);
                }
            }

            for (Scanner adjacentScanner : this.adjacentScanners) {
                adjacentScanner.fetchAdjacentScanners(remainingScanners);
            }

            possiblyAdjacentScanners.clear();
            possiblyAdjacentScanners.addAll(remainingScanners);
        }

        public Set<int[]> getOverlappingBeacons() {
            Set<int[]> overlappingBeacons = new TreeSet<>(comparator);
            overlappingBeacons.addAll(this.beacons);

            for (Scanner scanner : adjacentScanners) {
                Transformation t = getTrafo(scanner, this);
                trafos.put(scanner.name, t);
                overlappingBeacons.addAll(t.transform(scanner.getOverlappingBeacons()));
            }

            return overlappingBeacons;
        }

        public int[] getOrigin(Scanner scanner) {
            if (this.name.equals(scanner.name)) {
                return new int[] {0,0,0};
            }

            for (Scanner s : adjacentScanners) {
                int[] origin = s.getOrigin(scanner);
                if (origin != null) {
                    return trafos.get(s.name).transform(origin);
                }
            }

            return null;
        }
    }


    public static Transformation getTrafo(Scanner scannerB, Scanner scannerA) {

        Set<Integer> distancesA      = scannerA.getDistances();
        Set<Integer> distancesB      = scannerB.getDistances();
        Set<Integer> commonDistances = Sets.intersection(distancesA, distancesB);

        Set<int[]> commonBeaconsScannerA = scannerA.getDedicatedBeacons(commonDistances);

        int[] originA = commonBeaconsScannerA.iterator().next();
        Map<Integer, int[]> distancesToOriginA = commonBeaconsScannerA
                .stream()
                .collect(
                        toMap(
                                beacon -> distance(beacon, originA), identity()
                        )
                );
        distancesToOriginA.remove(0);


        Set<int[]> commonBeaconsScannerB = scannerB.getDedicatedBeacons(distancesToOriginA.keySet());
        Map<int[], Long> xx = scannerB.distList.entrySet()
                .stream()
                .filter(entry -> distancesToOriginA.containsKey(entry.getValue()))
                .map(Map.Entry::getKey)
                .flatMap(pair -> Stream.of(pair.getLeft(), pair.getRight()))
                .collect(groupingBy(identity(), counting()));

        long maxCount = xx.values().stream().max(Long::compareTo).orElse(0L);
        int[] originB = xx.entrySet().stream()
                .filter(entry -> entry.getValue() == maxCount)
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getKey();

        Map<Integer, int[]> distancesToOriginB = commonBeaconsScannerB
                .stream()
                .collect(
                        toMap(
                                beacon -> distance(beacon, originB), identity()
                        )
                );
        distancesToOriginB.remove(0);


        List<RealVector> delta_a = new ArrayList<>();
        List<RealVector> delta_b = new ArrayList<>();
        for (int distance : distancesToOriginA.keySet()) {
            double[] x;

            final int[] beaconA = distancesToOriginA.get(distance);
            x = new double[4];
            for (int n = 0; n < x.length-1; n++) {
                x[n] = beaconA[n];
            }
            x[3] = 1;
            delta_a.add(new ArrayRealVector(x));

            final int[] beaconB = distancesToOriginB.get(distance);
            x = new double[4];
            for (int n = 0; n < x.length-1; n++) {
                x[n] = beaconB[n];
            }
            x[3] = 1;
            delta_b.add(new ArrayRealVector(x));
        }

        RealVector a0 = delta_a.get(0);
        RealVector a1 = delta_a.get(1);
        RealVector a2 = delta_a.get(2);
        RealVector a3 = delta_a.get(3);
        RealVector b0 = delta_b.get(0);
        RealVector b1 = delta_b.get(1);
        RealVector b2 = delta_b.get(2);
        RealVector b3 = delta_b.get(3);

        RealMatrix A = new Array2DRowRealMatrix(4, 4);
        A.setColumnVector(0, a0);
        A.setColumnVector(1, a1);
        A.setColumnVector(2, a2);
        A.setColumnVector(3, a3);

        RealMatrix B = new Array2DRowRealMatrix(4, 4);
        B.setColumnVector(0, b0);
        B.setColumnVector(1, b1);
        B.setColumnVector(2, b2);
        B.setColumnVector(3, b3);

        RealMatrix B_inverse = null;

        DecompositionSolver solver;
        if ((solver = new LUDecomposition(B).getSolver()).isNonSingular()) {
            B_inverse = solver.getInverse();
        } else {
            System.out.println("===================");
        }

        RealMatrix X = A.multiply(B_inverse);
        double[][] data = X.getData();
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = round(data[i][j]);
            }
        }

        X = new Array2DRowRealMatrix(data);
//        System.out.println(new LUDecomposition(X).getDeterminant());

        return new Transformation(X);
    }

    private static int distance(int[] a, int[] b) {
        return (a[0]-b[0])*(a[0]-b[0]) + (a[1]-b[1])*(a[1]-b[1]) + (a[2]-b[2])*(a[2]-b[2]);
    }

    private int manhattanDistance(int[] originScannerA, int[] originScannerB) {
        int manhattanDistance = 0;
        for (int i = 0; i < originScannerA.length; i++) {
            manhattanDistance += Math.abs(originScannerA[i] - originScannerB[i]);
        }
        return manhattanDistance;
    }


    private static class Transformation {

        RealMatrix trafo;

        public Transformation(RealMatrix trafo) {
            this.trafo = trafo;
        }

        public Collection<int[]> transform(Collection<int[]> beacons) {
            return beacons.stream()
                    .map(b -> {
                        double[] v = {b[0], b[1], b[2], 1};
                        v = trafo.operate(v);
                        return new int[]{(int) round(v[0]), (int) round(v[1]), (int) round(v[2])};
                    })
                    .collect(toList());
        }

        public int[] transform(int[] coords) {
            double[] v = {coords[0], coords[1], coords[2], 1};
            v = trafo.operate(v);
            return new int[]{(int) round(v[0]), (int) round(v[1]), (int) round(v[2])};
        }
    }

}
