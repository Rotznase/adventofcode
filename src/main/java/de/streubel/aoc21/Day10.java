package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.stream.Collectors;


public class Day10 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        long totalScore;

        totalScore = stringInput.stream()
                .map(ChunkLine::new)
                .filter(ChunkLine::isIncorrect)
                .mapToLong(ChunkLine::getIncorrectScore)
                .sum();
        System.out.println("Result Part 1: "+totalScore);

        
        List<Long> completions = stringInput.stream()
                .map(ChunkLine::new)
                .filter(ChunkLine::isIncomplete)
                .mapToLong(ChunkLine::getIncompleteScore)
                .boxed()
                .sorted(Long::compareTo)
                .collect(Collectors.toList());
        totalScore = completions.get(completions.size() / 2);
        System.out.println("Result Part 2: "+totalScore);
    }

    private enum ValidationResult { OK, INCORRENT, INCOMPLETE }

    private static class ChunkLine {
        String line;
        ValidationResult validationResult = null;
        String cause = "";

        public ChunkLine(String line) {
            this.line = line;
            validate(this.line);
        }

        private void validate(String line) {
            Stack<Character> openingChars = new Stack<>();
            int i = 0;
            for (; i < line.toCharArray().length; i++) {
                char c = line.toCharArray()[i];
                if ("([{<".contains(""+c)) {
                    openingChars.push(c);
                } else if (")]}>".contains("" + c)) {
                    if (!openingChars.isEmpty()) {
                        final Character openingChar = openingChars.pop();
                        if (getMatchingCounterPart(openingChar) != c) {
                            validationResult = ValidationResult.INCORRENT;
                            cause = ""+c;
                            break;
                        }
                    }
                }
            }

            if (validationResult == null && openingChars.size() > 0) {
                validationResult = ValidationResult.INCOMPLETE;

                char[] closingSequence = new char[openingChars.size()];
                for (i = 0; i < closingSequence.length; i++) {
                    closingSequence[i] = getMatchingCounterPart(openingChars.pop());
                }
                cause = String.valueOf(closingSequence);
            }
        }

        public boolean isIncorrect() {
            return validationResult == ValidationResult.INCORRENT;
        }

        public boolean isIncomplete() {
            return validationResult == ValidationResult.INCOMPLETE;
        }

        public long getIncorrectScore() {
            switch (cause.toCharArray()[0]) {
                case ')': return 3;
                case ']': return 57;
                case '}': return 1197;
                case '>': return 25137;
            }
            return -1;
        }

        public long getIncompleteScore() {
            long score = 0;
            for (char c : cause.toCharArray()) {
                switch (c) {
                    case ')': score = 5 * score + 1; break;
                    case ']': score = 5 * score + 2; break;
                    case '}': score = 5 * score + 3; break;
                    case '>': score = 5 * score + 4; break;
                }
            }
            return score;
        }

        private Character getMatchingCounterPart(Character c) {
            switch (c) {
                case '(': return ')';
                case '[': return ']';
                case '{': return '}';
                case '<': return '>';
            }
            return 0;
        }
    }
}
