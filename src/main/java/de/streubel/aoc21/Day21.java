package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.List;


public class Day21 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        int pos1 = Integer.parseInt(stringInput.get(0).split("Player 1 starting position: ")[1]);
        int pos2 = Integer.parseInt(stringInput.get(1).split("Player 2 starting position: ")[1]);

        int score1 = 0;
        int score2 = 0;


        int r = 0;

        while (true) {
            r += 3;
            pos1 = (pos1 + 3*(r - 1) + 9) % 10 + 1;
            score1 += pos1;

            if (score1 >= 1000) {
                break;
            }

            r += 3;
            pos2 = (pos2 + 3*(r - 1) + 9) % 10 + 1;
            score2 += pos2;

            if (score2 >= 1000) {
                break;
            }
        }

        int resultPart1 = 0;
        if (score1 < 1000) {
            resultPart1 = score1 * r;
        }

        if (score2 < 1000) {
            resultPart1 = score2 * r;
        }

        System.out.println("Result Part 1: "+resultPart1);


        System.out.println("Result Part 2: ");
    }

}
