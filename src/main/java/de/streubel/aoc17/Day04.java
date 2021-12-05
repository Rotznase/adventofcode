package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day04 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        String[] passphrases = stringInput.toArray(new String[0]);

        // Teil a
        int validCounter = 0;
        for (String passphrase: passphrases) {
            final String[] words = passphrase.split("\\s+");
            Set<String> wordSet = new HashSet<>(Arrays.asList(words));
            if (words.length == wordSet.size()) {
                validCounter++;
            }
        }

        System.out.println(validCounter);

        // Teil b
        validCounter = 0;
        for (String passphrase: passphrases) {
            final String[] words = passphrase.split("\\s+");
            for (int i=0; i<words.length; i++) {
                final char[] a = words[i].toCharArray();
                Arrays.sort(a);
                words[i] = new String(a);
            }

            Set<String> wordSet = new HashSet<>(Arrays.asList(words));
            if (words.length == wordSet.size()) {
                validCounter++;
            }
        }

        System.out.println(validCounter);

    }
}
