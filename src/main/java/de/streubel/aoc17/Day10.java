package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day10 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        // Teil 1
        {
            int[] lengths = {14, 58, 0, 116, 179, 16, 1, 104, 2, 254, 167, 86, 255, 55, 122, 244};
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < 256; i++) {
                list.add(i);
            }
            int currentPos = 0;
            int skipSize = 0;

            for (final int length : lengths) {
                Collections.rotate(list, -currentPos);
                Collections.reverse(list.subList(0, length));
                Collections.rotate(list, +currentPos);
                currentPos += length + skipSize;
                skipSize++;
            }

            int result = list.get(0) * list.get(1);
            System.out.println(result);
        }

        // Teil 2
        {
            String input = "14,58,0,116,179,16,1,104,2,254,167,86,255,55,122,244";
            final String hash = knotHash(input);
            System.out.println(hash);
        }
    }

    public String knotHash(String input) {
        int[] lengths = new int[input.length() + 5];
        int i = 0;
        for (byte b : input.getBytes())
            lengths[i++] = b;
        lengths[i++] = 17;
        lengths[i++] = 31;
        lengths[i++] = 73;
        lengths[i++] = 47;
        lengths[i++] = 23;

        List<Integer> list = new ArrayList<>();
        for (i = 0; i < 256; i++) {
            list.add(i);
        }
        int currentPos = 0;
        int skipSize = 0;

        for (i = 0; i < 64; i++) {
            for (final int length : lengths) {
                Collections.rotate(list, -currentPos);
                Collections.reverse(list.subList(0, length));
                Collections.rotate(list, +currentPos);
                currentPos += length + skipSize;
                skipSize++;
            }
        }

        List<Integer> sparseHash = list;
        int[] denseHash = new int[16];
        for (i = 0; i < 16; i++) {
            denseHash[i] = 0;
            for (int x : sparseHash.subList(i * 16, i * 16 + 16)) {
                denseHash[i] ^= x;
            }
        }

        StringBuilder hash = new StringBuilder();
        for (i = 0; i < 16; i++) {
            String s = String.format("%02x", denseHash[i]);
            hash.append(s);
        }

        return hash.toString();
    }
}
