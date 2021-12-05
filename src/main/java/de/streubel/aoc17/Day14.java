package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Day14 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
//        String input = "flqrgnkx";
        String input = "nbysizxe";
        Day10 day10 = new Day10();

        char[][] disk = new char[128][128];
        for (int i=0; i<128; i++) {
            String hashInput = input+"-"+i;
            String hash = day10.knotHash(hashInput);

            char[] bits = new BigInteger(hash, 16).toString(2).toCharArray();
            Arrays.fill(disk[i], '0');
            System.arraycopy(bits, 0, disk[i], 128-bits.length, bits.length);
        }

        // Teil 1
        int count = 0;
        for (char[] row : disk) {
            for (char bit : row)
                count += bit - '0';
        }
        System.out.println(count);


        // Teil 2
        Collection<P> m = new ArrayList<>();
        for (int i=0; i<128; i++) {
            for (int j=0; j<128; j++) {
                m.add(new P(i+1, j+1, disk));
            }
        }

        final List<Set<P>> regions = new ArrayList<>();
        m.stream().filter(P::used).forEach(p -> {
            final Set<P> adjacent = p.adjacent(true);

            Collection<Set<P>> found = regions.stream().filter(region -> !Collections.disjoint(region, adjacent))
                    .collect(Collectors.toList());

            if (!found.isEmpty()) {
                final Set<P> first = found.stream().findFirst().orElse(null);
                assert first != null;
                found.stream().skip(1).forEach(set -> {
                    first.addAll(set);
                    regions.remove(set);
                });

                first.addAll(adjacent);
            } else {
                regions.add(adjacent);
            }
        });

        System.out.println(regions.size());
    }


    private void add(P p, Set<P> region) {
        if (!p.used() || region.contains(p))
            return;
        region.add(p);
        add(p.north(), region);
        add(p.west(),  region);
        add(p.south(), region);
        add(p.east(),  region);
    }

    private class P {
        char[][] matrix;
        int i, j;

        P(int i, int j, char[][] matrix) {
            this.i = i;
            this.j = j;
            this.matrix = matrix;
        }

        boolean used() {
            return matrix[j-1][i-1] == '1';
        }

        P south() {
            return j + 1 <= 128 ? new P(i, j + 1, matrix) : null;
        }
        P north() {
            return j - 1 >= 1 ? new P(i, j - 1, matrix) : null;
        }
        P west() {
            return i - 1 >= 1 ? new P(i - 1, j, matrix) : null;
        }
        P east() {
            return i + 1 <= 128 ? new P(i + 1, j, matrix) : null;
        }

        Set<P> adjacent(boolean includeSelf) {
            Set<P> set = new HashSet<>();
            P p;

            p = west();
            if (p != null && p.used())
                set.add(p);
            p = east();
            if (p != null && p.used())
                set.add(p);
            p = north();
            if (p != null && p.used())
                set.add(p);
            p = south();
            if (p != null && p.used())
                set.add(p);
            if (includeSelf && used())
                set.add(this);
            return set;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            P p = (P) o;

            if (i != p.i) return false;
            return j == p.j;
        }

        @Override
        public int hashCode() {
            int result = i;
            result = 31 * result + j;
            return result;
        }

        @Override
        public String toString() {
            return "(" + i + ", " + j + ")";
        }
    }
}
