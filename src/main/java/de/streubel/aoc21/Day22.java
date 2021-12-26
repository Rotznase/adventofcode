package de.streubel.aoc21;

import com.google.common.collect.Range;
import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.collect.Range.closed;


public class Day22 extends AdventOfCodeRunner {

    private static final Pattern pattern = Pattern.compile("(on|off) x=(-*\\d+)..(-*\\d+),y=(-*\\d+)..(-*\\d+),z=(-*\\d+)..(-*\\d+)");

    @Override
    public void run(List<String> stringInput) {
        System.out.println("Result Part 1: "+part1(stringInput));
        System.out.println("Result Part 2: "+part2(stringInput));
    }

    private long part1(List<String> stringInput) {
        Set<Vector3D> cubes = new HashSet<>();
        for (String line : stringInput) {
            final Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                final String onOrOff = matcher.group(1);
                final int x1 = Integer.parseInt(matcher.group(2));
                final int x2 = Integer.parseInt(matcher.group(3));
                final int y1 = Integer.parseInt(matcher.group(4));
                final int y2 = Integer.parseInt(matcher.group(5));
                final int z1 = Integer.parseInt(matcher.group(6));
                final int z2 = Integer.parseInt(matcher.group(7));

                for (int x = x1; x <= x2; x++) {
                    if (x < -50 || x > 50) {
                        continue;
                    }
                    for (int y = y1; y <= y2; y++) {
                        if (y < -50 || y > 50) {
                            continue;
                        }
                        for (int z = z1; z <= z2; z++) {
                            if (z < -50 || z > 50) {
                                continue;
                            }

                            Vector3D cube = new Vector3D(x, y, z);
                            if (onOrOff.equals("on")) {
                                cubes.add(cube);
                            } else {
                                cubes.remove(cube);
                            }
                        }
                    }
                }

            }
        }

        return cubes.size();
    }

    private long part2(List<String> stringInput) {

        Set<Cube> onCubes = new HashSet<>();

        for (String line : stringInput) {
            final Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                final String onOrOff = matcher.group(1);
                final int x1 = Integer.parseInt(matcher.group(2));
                final int x2 = Integer.parseInt(matcher.group(3));
                final int y1 = Integer.parseInt(matcher.group(4));
                final int y2 = Integer.parseInt(matcher.group(5));
                final int z1 = Integer.parseInt(matcher.group(6));
                final int z2 = Integer.parseInt(matcher.group(7));

                Cube newCube = new Cube(x1, x2, y1, y2, z1, z2);

                if (onCubes.isEmpty()) {
                    if (onOrOff.equals("on")) {
                        onCubes.add(newCube);
                    }
                } else {

                    final Triple<Set<Cube>, Set<Cube>, Set<Cube>> split = Cube.split(onCubes, newCube);

                    onCubes.clear();

                    if (onOrOff.equals("on")) {
                        onCubes.addAll(split.getLeft());
                        onCubes.addAll(split.getMiddle());
                        onCubes.addAll(split.getRight());
                    } else {
                        onCubes.addAll(split.getLeft());
                    }
                }
            }
        }

        long nrOfOns = 0;
        for (Cube cube : onCubes) {
            nrOfOns += cube.size();
        }

        return nrOfOns;
    }

    public static class Cube {
        Range<Integer> x;
        Range<Integer> y;
        Range<Integer> z;

        public Cube(int x1, int x2, int y1, int y2, int z1, int z2) {
            x = closed(x1, x2);
            y = closed(y1, y2);
            z = closed(z1, z2);
        }

        public Cube(Range<Integer> x, Range<Integer> y, Range<Integer> z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public long size() {
            return (long) (x.upperEndpoint() - x.lowerEndpoint() + 1)
                        * (y.upperEndpoint() - y.lowerEndpoint() + 1)
                        * (z.upperEndpoint() - z.lowerEndpoint() + 1);
        }

        public Set<Cube> split(Cube intersection) {
            Set<Cube> cubes = new HashSet<>();
            for (Range<Integer> a : split(x, intersection.x)) {
                for (Range<Integer> b : split(y, intersection.y)) {
                    for (Range<Integer> c : split(z, intersection.z)) {
                        cubes.add(new Cube(a, b,c));
                    }
                }
            }

            return cubes;
        }

        public Set<Cube> minus(Cube cubeToSubtract) {
            Set<Cube> difference = new HashSet<>();
            if (this.intersects(cubeToSubtract)) {
                final Cube intersection = this.intersection(cubeToSubtract);
                difference.addAll(split(intersection));
                difference.remove(intersection);
            } else {
                difference.add(this);
            }

            return difference;
        }

        private Set<Range<Integer>> split(Range<Integer> r1, Range<Integer> r2) {

            Set<Range<Integer>> rangeSet = new HashSet<>();

            if (r1.lowerEndpoint().intValue() == r1.upperEndpoint() && r2.lowerEndpoint().intValue() == r2.upperEndpoint()) {
                if (r1.lowerEndpoint().intValue() != r2.lowerEndpoint()) {
                    rangeSet = Collections.emptySet();
                } else {
                    rangeSet.add(r1);
                }
                return rangeSet;
            } else if (r1.lowerEndpoint().intValue() == r1.upperEndpoint()) {
                if (r1.lowerEndpoint() < r2.lowerEndpoint()) {
                    rangeSet = Collections.emptySet();
                } else if (r1.lowerEndpoint().intValue() == r2.lowerEndpoint()) {
                    rangeSet.add(r1);
                    rangeSet.add(closed(r1.upperEndpoint()+1, r2.upperEndpoint()));
                } else if (r2.lowerEndpoint() < r1.lowerEndpoint() && r1.upperEndpoint() < r2.upperEndpoint()) {
                    rangeSet.add(closed(r2.lowerEndpoint(), r1.lowerEndpoint()-1));
                    rangeSet.add(r1);
                    rangeSet.add(closed(r1.upperEndpoint()+1, r2.upperEndpoint()));
                } else if (r1.lowerEndpoint().intValue() == r2.upperEndpoint()) {
                    rangeSet.add(closed(r2.lowerEndpoint(), r1.lowerEndpoint()-1));
                    rangeSet.add(r1);
                } else if (r2.upperEndpoint() < r1.lowerEndpoint()) {
                    rangeSet = Collections.emptySet();
                }
                return rangeSet;
            } else if (r2.lowerEndpoint().intValue() == r2.upperEndpoint()) {
                if (r2.lowerEndpoint() < r1.lowerEndpoint()) {
                    rangeSet = Collections.emptySet();
                } else if (r2.lowerEndpoint().intValue() == r1.lowerEndpoint()) {
                    rangeSet.add(r2);
                    rangeSet.add(closed(r2.upperEndpoint()+1, r1.upperEndpoint()));
                } else if (r1.lowerEndpoint() < r2.lowerEndpoint() && r2.upperEndpoint() < r1.upperEndpoint()) {
                    rangeSet.add(closed(r1.lowerEndpoint(), r2.lowerEndpoint()-1));
                    rangeSet.add(r2);
                    rangeSet.add(closed(r2.upperEndpoint()+1, r1.upperEndpoint()));
                } else if (r2.lowerEndpoint().intValue() == r1.upperEndpoint()) {
                    rangeSet.add(closed(r1.lowerEndpoint(), r2.lowerEndpoint()-1));
                    rangeSet.add(r2);
                } else if (r1.upperEndpoint() < r2.lowerEndpoint()) {
                    rangeSet = Collections.emptySet();
                }
                return rangeSet;
            }

            if (r1.upperEndpoint() < r2.lowerEndpoint()) {
                rangeSet = Collections.emptySet();
            } else if (r1.upperEndpoint().intValue() == r2.lowerEndpoint()) {
                rangeSet.add(closed(r1.lowerEndpoint(), r1.upperEndpoint()-1));
                rangeSet.add(closed(r1.upperEndpoint(), r2.lowerEndpoint()));
                rangeSet.add(closed(r2.lowerEndpoint()+1, r2.upperEndpoint()));
            } else if (r1.lowerEndpoint() < r2.lowerEndpoint() && r2.lowerEndpoint() < r1.upperEndpoint() && r1.upperEndpoint() < r2.upperEndpoint()) {
                rangeSet.add(closed(r1.lowerEndpoint(), r2.lowerEndpoint()-1));
                rangeSet.add(closed(r2.lowerEndpoint(), r1.upperEndpoint()));
                rangeSet.add(closed(r1.upperEndpoint()+1, r2.upperEndpoint()));
            } else if (r1.lowerEndpoint() < r2.lowerEndpoint() && r1.upperEndpoint().intValue() == r2.upperEndpoint()) {
                rangeSet.add(closed(r1.lowerEndpoint(), r2.lowerEndpoint()-1));
                rangeSet.add(closed(r2.lowerEndpoint(), r2.upperEndpoint()));
            } else if (r1.lowerEndpoint().intValue() == r2.lowerEndpoint() && r1.upperEndpoint() < r2.upperEndpoint()) {
                rangeSet.add(closed(r1.lowerEndpoint(), r1.upperEndpoint()));
                rangeSet.add(closed(r1.upperEndpoint()+1, r2.upperEndpoint()));
            } else if (r1.lowerEndpoint() < r2.lowerEndpoint() && r2.upperEndpoint() < r1.upperEndpoint()) {
                rangeSet.add(closed(r1.lowerEndpoint(), r2.lowerEndpoint()-1));
                rangeSet.add(closed(r2.lowerEndpoint(), r2.upperEndpoint()));
                rangeSet.add(closed(r2.upperEndpoint()+1, r1.upperEndpoint()));
            } else if (r1.lowerEndpoint().intValue() == r2.lowerEndpoint() && r1.upperEndpoint().intValue() == r2.upperEndpoint()) {
                rangeSet.add(r1);
            }

            else // r1 und r2 vertauscht

            if (r2.upperEndpoint() < r1.lowerEndpoint()) {
                rangeSet = Collections.emptySet();
            } else if (r2.upperEndpoint().intValue() == r1.lowerEndpoint()) {
                rangeSet.add(closed(r2.lowerEndpoint(), r2.upperEndpoint()-1));
                rangeSet.add(closed(r2.upperEndpoint(), r1.lowerEndpoint()));
                rangeSet.add(closed(r1.lowerEndpoint()+1, r1.upperEndpoint()));
            } else if (r2.lowerEndpoint() < r1.lowerEndpoint() && r1.lowerEndpoint() < r2.upperEndpoint() && r2.upperEndpoint() < r1.upperEndpoint()) {
                rangeSet.add(closed(r2.lowerEndpoint(), r1.lowerEndpoint()-1));
                rangeSet.add(closed(r1.lowerEndpoint(), r2.upperEndpoint()));
                rangeSet.add(closed(r2.upperEndpoint()+1, r1.upperEndpoint()));
            } else if (r2.lowerEndpoint() < r1.lowerEndpoint() && r2.upperEndpoint().intValue() == r1.upperEndpoint()) {
                rangeSet.add(closed(r2.lowerEndpoint(), r1.lowerEndpoint()-1));
                rangeSet.add(closed(r1.lowerEndpoint(), r1.upperEndpoint()));
            } else if (r2.lowerEndpoint().intValue() == r1.lowerEndpoint() && r2.upperEndpoint() < r1.upperEndpoint()) {
                rangeSet.add(closed(r2.lowerEndpoint(), r2.upperEndpoint()));
                rangeSet.add(closed(r2.upperEndpoint()+1, r1.upperEndpoint()));
            } else if (r2.lowerEndpoint() < r1.lowerEndpoint() && r1.upperEndpoint() < r2.upperEndpoint()) {
                rangeSet.add(closed(r2.lowerEndpoint(), r1.lowerEndpoint()-1));
                rangeSet.add(closed(r1.lowerEndpoint(), r1.upperEndpoint()));
                rangeSet.add(closed(r1.upperEndpoint()+1, r2.upperEndpoint()));
            } else if (r2.lowerEndpoint().intValue() == r1.lowerEndpoint() && r2.upperEndpoint().intValue() == r1.upperEndpoint()) {
                rangeSet.add(r2);
            } else {
                throw new RuntimeException();
            }
            
            return rangeSet;
        }

        public boolean intersects(Cube other) {
            return x.isConnected(other.x) && !x.intersection(other.x).isEmpty() &&
                   y.isConnected(other.y) && !y.intersection(other.y).isEmpty() &&
                   z.isConnected(other.z) && !z.intersection(other.z).isEmpty();
        }


        public Cube intersection(Cube other) {
            return new Cube(
                x.intersection(other.x),
                y.intersection(other.y),
                z.intersection(other.z)
            );
        }

        @Override
        public String toString() {
            return String.format("[%d,%d][%d,%d][%d,%d]",
                    x.lowerEndpoint(), x.upperEndpoint(),
                    y.lowerEndpoint(), y.upperEndpoint(),
                    z.lowerEndpoint(), z.upperEndpoint());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Cube cube = (Cube) o;
            return x.equals(cube.x) && y.equals(cube.y) && z.equals(cube.z);
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }




        public static Triple<Set<Cube>, Set<Cube>, Set<Cube>> split(Set<Cube> left, Cube right) {
            Set<Cube> newLeft = new HashSet<>();
            Set<Cube> newMiddle = new HashSet<>();
            Set<Cube> newRight = new HashSet<>();
            newRight.add(right);

            for (Cube c : left) {
                newLeft.addAll(c.minus(right));
                if (c.intersects(right)) {
                    newMiddle.add(c.intersection(right));
                }
                newRight = Cube.minus(newRight, c);
            }

            return Triple.of(newLeft, newMiddle, newRight);
        }



        public static Set<Cube> minus(Set<Cube> fragments, Cube cubeToSubtract) {
            Set<Cube> newFragments = new HashSet<>();
            for (Cube fragment : fragments) {
                Set<Cube> differences = fragment.minus(cubeToSubtract);
                if (!differences.isEmpty()) {
                    newFragments.addAll(differences);
                }
            }
            return newFragments;
        }

    }
}
