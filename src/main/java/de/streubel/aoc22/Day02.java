package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class Day02 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        int totalScore;

        totalScore = 0;
        for (String input : stringInput) {
            GameRound gameRound = new GameRound(input);
            totalScore += gameRound.score1();
        }
        
        System.out.println("Result Part 1 (17189): "+ totalScore);

        totalScore = 0;
        for (String input : stringInput) {
            GameRound gameRound = new GameRound(input);
            totalScore += gameRound.score2();
        }

        System.out.println("Result Part 2 (13490): "+totalScore);

    }


    private static final char ROCK = 'A';
    private static final char PAPER = 'B';
    private static final char SCISSORS = 'C';

    private static final int SCORE_A_ROCK = 1;
    private static final int SCORE_B_PAPER = 2;
    private static final int SCORE_C_SCISSORS = 3;
    private static final int SCORE_LOOSE = 0;
    private static final int SCORE_DRAW = 3;
    private static final int SCORE_WIN = 6;

    private static class GameRound {
        private char opponent;
        private char myself;

        public GameRound(final String gameRound) {
            final String[] s = gameRound.split(" ");
            opponent = s[0].charAt(0);
            myself = s[1].charAt(0);
        }

        public int score1() {
            int score = 0;
            switch (opponent) {
                case ROCK:
                    switch (myself) {
                        case 'X': score += SCORE_A_ROCK + SCORE_DRAW; break;
                        case 'Y': score += SCORE_B_PAPER + SCORE_WIN; break;
                        case 'Z': score += SCORE_C_SCISSORS + SCORE_LOOSE; break;
                    }
                    break;

                case PAPER:
                    switch (myself) {
                        case 'X': score += SCORE_A_ROCK + SCORE_LOOSE; break;
                        case 'Y': score += SCORE_B_PAPER + SCORE_DRAW; break;
                        case 'Z': score += SCORE_C_SCISSORS + SCORE_WIN; break;
                    }
                    break;

                case SCISSORS:
                    switch (myself) {
                        case 'X': score += SCORE_A_ROCK + SCORE_WIN; break;
                        case 'Y': score += SCORE_B_PAPER + SCORE_LOOSE; break;
                        case 'Z': score += SCORE_C_SCISSORS + SCORE_DRAW; break;
                    }
                    break;
            }

            return score;
        }

        public int score2() {
            int score = 0;
            switch (opponent) {
                case ROCK:
                    switch (myself) {
                        case 'X': score += SCORE_LOOSE + SCORE_C_SCISSORS; break;
                        case 'Y': score += SCORE_DRAW +SCORE_A_ROCK; break;
                        case 'Z': score += SCORE_WIN + SCORE_B_PAPER; break;
                    }
                    break;

                case PAPER:
                    switch (myself) {
                        case 'X': score += SCORE_LOOSE + SCORE_A_ROCK; break;
                        case 'Y': score += SCORE_DRAW + SCORE_B_PAPER; break;
                        case 'Z': score += SCORE_WIN + SCORE_C_SCISSORS; break;
                    }
                    break;

                case SCISSORS:
                    switch (myself) {
                        case 'X': score += SCORE_LOOSE + SCORE_B_PAPER; break;
                        case 'Y': score += SCORE_DRAW + SCORE_C_SCISSORS; break;
                        case 'Z': score += SCORE_WIN + SCORE_A_ROCK; break;
                    }
                    break;
            }

            return score;
        }
    }
}
