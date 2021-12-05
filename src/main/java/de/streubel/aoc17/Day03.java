package de.streubel.aoc17;

import com.google.common.base.Objects;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.streubel.AdventOfCodeRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day03 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        final int inpugt = Integer.parseInt(stringInput.get(0));

        BiMap<Integer, Coords> mem = HashBiMap.create();
        BiMap<Coords, Integer> rev = mem.inverse();

        // Teil 1:
        Coords v = Coords.E;
        Coords x = new Coords(0, 0);

        mem.put(1, x);

        for (int i = 2; i <= inpugt; i++) {
            x = x.plus(v);
            mem.put(i, x);

            if (rev.get(x.plus(v.left())) == null) {
                v = v.left();
            }
        }

        final Coords first = mem.get(1);
        final Coords last = mem.get(inpugt);
        int length = Math.abs(first.x - last.x) + Math.abs(first.y - last.y);
        System.out.println(length);

        // Teil 2:
        Map<Coords, Integer> mem2 = new HashMap<>();
        v = Coords.E;
        x = new Coords(0, 0);

        mem2.put(x, 1);

        int i=0;
        while (i <= inpugt) {
            i = 0;
            x = x.plus(v);
            for (Coords d: Coords.alldirs) {
                Integer u = mem2.get(x.plus(d));
                if (u != null) {
                    i += u;
                }
            }
            mem2.put(x, i);

            if (mem2.get(x.plus(v.left())) == null) {
                v = v.left();
            }
        }
        System.out.println(i);

    }

    private static class Coords {
        private static Coords N  = new Coords( 0,  1);
        private static Coords NW = new Coords(-1,  1);
        private static Coords W  = new Coords(-1,  0);
        private static Coords SW = new Coords(-1, -1);
        private static Coords S  = new Coords( 0, -1);
        private static Coords SE = new Coords( 1, -1);
        private static Coords E  = new Coords( 1,  0);
        private static Coords NE = new Coords( 1,  1);

        static Coords[] alldirs = {N,NW,W,SW,S,SE,E,NE};

        int x, y;

        public Coords(final int x, final int y) {
            this.x = x;
            this.y = y;
        }

        public Coords plus(final Coords coords) {
            return new Coords(this.x + coords.x, this.y + coords.y);
        }

        public Coords left() {
            if (this == N) return W;
            if (this == W) return S;
            if (this == S) return E;
            if (this == E) return N;
            return null;
        }


        @Override
        public boolean equals(Object c) {
            Coords a = this;
            Coords b = (Coords) c;
            return a.x == b.x && a.y == b.y;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(x, y);
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ")";
        }
    }


}

