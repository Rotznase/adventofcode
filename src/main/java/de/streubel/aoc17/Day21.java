package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day21 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {

        Pattern pattern = Pattern.compile("(.*) => (.*)");

        Map<Image, Image> enhancementRules = new HashMap<>();
        for (String s: input) {
            final Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                Image key = Image.parse(matcher.group(1));
                Image rule = Image.parse(matcher.group(2));
                enhancementRules.put(key, rule);
                enhancementRules.put(key.rotateR(), rule);
                enhancementRules.put(key.rotateL(), rule);
                enhancementRules.put(key.rotateL().rotateL(), rule);
                key = key.flipH();
                enhancementRules.put(key, rule);
                enhancementRules.put(key.rotateR(), rule);
                enhancementRules.put(key.rotateL(), rule);
                enhancementRules.put(key.rotateL().rotateL(), rule);
            }

        }



        for (int iterations: Arrays.asList(5,18)) {

            Image image = Image.parse(".#./..#/###");

            for (int i = 0; i < iterations; i++) {
                for (int mod : Arrays.asList(2, 3)) {
                    if (image.size() % mod == 0) {
                        Image[][] grid = image.split(mod);
                        for (int r = 0; r < grid.length; r++) {
                            for (int c = 0; c < grid[r].length; c++) {
                                grid[r][c].transform(enhancementRules);
                            }
                        }
                        image.join(grid);
                        break;
                    }
                }
            }

            System.out.println(image.count('#'));
        }
    }


    static class Image {
        Character[][] bits;

        public Image(int size) {
            bits = new Character[size][size];
            for (int r=0; r<bits.length; r++) {
                Arrays.fill(bits[r], '.');
            }
        }

        static Image parse(String data) {
            final String[] lines = data.split("/");
            Image image = new Image(lines.length);
            for (int r=0; r<lines.length; r++) {
                final char[] chars = lines[r].toCharArray();
                for (int c=0; c<chars.length; c++) {
                    image.bits[r][c] = chars[c];
                }
            }
            return image;
        }


        int size() {
            return bits.length;
        }

        Image[][] split(int size) {
            final int gridSize = bits.length / size;
            Image[][] grid = new Image[gridSize][gridSize];

            for (int r=0; r<size(); r++) {
                for (int c=0; c<size(); c++) {
                    if (grid[r/size][c/size] == null) {
                        grid[r/size][c/size] = new Image(size);
                    }
                    grid[r/size][c/size].bits[r%size][c%size] = bits[r][c];
                }
            }

            return grid;
        }

        void transform(Map<Image, Image> enhancementRules) {
            Image image = enhancementRules.get(this);
            if (image == null) {
                image = new Image(this.size()+1);

            }
            bits = image.bits.clone();
        }

        void join(Image[][] grid) {
            final int squareSize = grid[0][0].size();
            int newSize = grid.length * squareSize;
            bits = new Character[newSize][newSize];
            for (int r=0; r<newSize; r++) {
                for (int c=0; c<newSize; c++) {
                    bits[r][c] = grid[r/squareSize][c/squareSize].bits[r%squareSize][c%squareSize];
                }
            }
        }

        int count(char x) {
            int cnt=0;
            for (int r=0; r<bits.length; r++)
                for (int c=0; c<bits.length; c++)
                    if (bits[r][c] == x)
                        cnt++;
            return cnt;
        }

        Image rotateL() {
            Image rotated = new Image(size());
            int maxIndex = bits.length-1;
            for (int r=0; r<bits.length; r++) {
                for (int c=0; c<bits[r].length; c++) {
                    rotated.bits[maxIndex-c][r] = bits[r][c];
                }
            }
            return rotated;
        }

        Image rotateR() {
            Image rotated = new Image(size());
            int maxIndex = bits.length-1;
            for (int r=0; r<bits.length; r++) {
                for (int c=0; c<bits[r].length; c++) {
                    rotated.bits[c][maxIndex-r] = bits[r][c];
                }
            }
            return rotated;
        }

        Image flipV() {
            Image flipped = new Image(size());
            int maxIndex = bits.length-1;
            for (int r=0; r<bits.length; r++) {
                for (int c=0; c<bits[r].length; c++) {
                    flipped.bits[maxIndex-r][c] = bits[r][c];
                }
            }
            return flipped;
        }

        Image flipH() {
            Image flipped = new Image(size());
            int maxIndex = bits.length-1;
            for (int r=0; r<bits.length; r++) {
                for (int c=0; c<bits[r].length; c++) {
                    flipped.bits[r][maxIndex-c] = bits[r][c];
                }
            }
            return flipped;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return Arrays.deepEquals(bits, image.bits);
        }

        @Override
        public int hashCode() {
            return Arrays.deepHashCode(bits);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Character[] row: bits) {
                for (Character col: row) {
                    sb.append(col);
                }
                sb.append("\n");
            }
            return sb.toString();
        }
    }

    String[] input = {
//        "../.# => ##./#../...",
//        ".#./..#/### => #..#/..../..../#..#",
            "../.. => ..#/#.#/###",
            "#./.. => .#./#../###",
            "##/.. => #.#/#.#/..#",
            ".#/#. => .##/..#/#..",
            "##/#. => #../#.#/#..",
            "##/## => #.#/.#./#..",
            ".../.../... => ####/##../#.../#...",
            "#../.../... => ##.#/####/.#../....",
            ".#./.../... => ...#/...#/#.../.##.",
            "##./.../... => ###./#.##/#..#/...#",
            "#.#/.../... => ##.#/###./.#.#/##..",
            "###/.../... => .###/#..#/..##/.##.",
            ".#./#../... => ####/#..#/#..#/#..#",
            "##./#../... => #.##/###./##../#...",
            "..#/#../... => ..##/.#.#/..#./.###",
            "#.#/#../... => .##./.#../..#./###.",
            ".##/#../... => ####/..../###./###.",
            "###/#../... => .#.#/.###/##.#/#..#",
            ".../.#./... => ..../.#../.##./..#.",
            "#../.#./... => #.##/..#./####/#.##",
            ".#./.#./... => .#.#/.###/#.#./.#.#",
            "##./.#./... => #..#/#.#./...#/.###",
            "#.#/.#./... => .##./#..#/####/.###",
            "###/.#./... => #.../..../.#.#/##..",
            ".#./##./... => #..#/..##/.##./.#.#",
            "##./##./... => ..##/#..#/####/###.",
            "..#/##./... => ####/.#.#/#.##/#.##",
            "#.#/##./... => .###/...#/#.../...#",
            ".##/##./... => ..##/.#.#/#.../##.#",
            "###/##./... => ##../..#./..#./#...",
            ".../#.#/... => .#.#/##../#..#/.#.#",
            "#../#.#/... => #.##/...#/##../...#",
            ".#./#.#/... => #.../..##/#..#/.##.",
            "##./#.#/... => .##./..##/.#../..#.",
            "#.#/#.#/... => .#../#..#/#.#./....",
            "###/#.#/... => ##.#/..##/##../#...",
            ".../###/... => #.../..#./##../#.##",
            "#../###/... => ..#./#.../##../.##.",
            ".#./###/... => ###./.#.#/..##/##.#",
            "##./###/... => ##.#/#.../##.#/#.#.",
            "#.#/###/... => ..##/...#/##../#..#",
            "###/###/... => ##.#/.###/...#/#..#",
            "..#/.../#.. => .##./#.##/..#./####",
            "#.#/.../#.. => ..#./###./#.../##.#",
            ".##/.../#.. => ...#/...#/.#../.###",
            "###/.../#.. => .##./.#../##../#.#.",
            ".##/#../#.. => ####/..##/#.../##..",
            "###/#../#.. => #.../#..#/####/##..",
            "..#/.#./#.. => .##./##.#/.#../###.",
            "#.#/.#./#.. => ..../.###/###./.#.#",
            ".##/.#./#.. => #.##/#..#/###./..#.",
            "###/.#./#.. => #.../..#./##../.#.#",
            ".##/##./#.. => .##./.#.#/#..#/#..#",
            "###/##./#.. => .#../.#.#/#..#/....",
            "#../..#/#.. => ####/..##/..##/.###",
            ".#./..#/#.. => ###./.###/..#./##.#",
            "##./..#/#.. => .###/####/#.../#.##",
            "#.#/..#/#.. => #.##/#..#/.#.#/...#",
            ".##/..#/#.. => #.../##../..##/##.#",
            "###/..#/#.. => ###./##.#/#.../.#..",
            "#../#.#/#.. => #.#./#.../##../..#.",
            ".#./#.#/#.. => .###/#.#./...#/##.#",
            "##./#.#/#.. => .#../#.##/##.#/#.#.",
            "..#/#.#/#.. => .#../#..#/.#../.#.#",
            "#.#/#.#/#.. => .#../.##./..../..#.",
            ".##/#.#/#.. => .##./.#../####/#.##",
            "###/#.#/#.. => ..#./##../##../#.#.",
            "#../.##/#.. => #.##/.##./..#./..##",
            ".#./.##/#.. => ###./#.#./#.../###.",
            "##./.##/#.. => ####/#.../#.../#.#.",
            "#.#/.##/#.. => .###/#..#/###./#..#",
            ".##/.##/#.. => #.../####/###./###.",
            "###/.##/#.. => .#../.#.#/##../.#..",
            "#../###/#.. => ..#./.##./.###/##..",
            ".#./###/#.. => ####/.##./####/....",
            "##./###/#.. => #.../#.../#.##/.##.",
            "..#/###/#.. => .#.#/.###/...#/....",
            "#.#/###/#.. => ###./..##/.#../#.##",
            ".##/###/#.. => ...#/.#../##../.#..",
            "###/###/#.. => ...#/#.##/.#.#/..##",
            ".#./#.#/.#. => .###/#.../..#./.##.",
            "##./#.#/.#. => ###./##.#/..#./##.#",
            "#.#/#.#/.#. => #.../##.#/..#./#...",
            "###/#.#/.#. => ...#/...#/#..#/...#",
            ".#./###/.#. => #.#./.##./#.#./.###",
            "##./###/.#. => #.../####/..##/#...",
            "#.#/###/.#. => ##../.##./.###/###.",
            "###/###/.#. => ..#./.##./.#../#.#.",
            "#.#/..#/##. => ...#/#.##/##../...#",
            "###/..#/##. => ...#/#.../###./###.",
            ".##/#.#/##. => ##.#/.#.#/.#../....",
            "###/#.#/##. => .##./..../##.#/..#.",
            "#.#/.##/##. => .#../###./#.#./##..",
            "###/.##/##. => #.##/#..#/#.#./###.",
            ".##/###/##. => #.##/###./..../##..",
            "###/###/##. => .#../####/.###/##..",
            "#.#/.../#.# => #.../#..#/..##/##.#",
            "###/.../#.# => #..#/.#.#/####/#.##",
            "###/#../#.# => ###./##../##.#/...#",
            "#.#/.#./#.# => .##./.#.#/#.../...#",
            "###/.#./#.# => .#../.#../..../#.#.",
            "###/##./#.# => #.#./#.#./#.../.#..",
            "#.#/#.#/#.# => ..../####/####/..#.",
            "###/#.#/#.# => #..#/.##./#.../##..",
            "#.#/###/#.# => ###./...#/#.##/##..",
            "###/###/#.# => #.##/#.../#..#/###.",
            "###/#.#/### => ..../...#/###./..#.",
            "###/###/### => #..#/..../#.../#.##",
    };
}
