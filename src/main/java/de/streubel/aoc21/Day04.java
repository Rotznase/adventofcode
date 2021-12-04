package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day04 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        List<Integer> drawnNumbers = Arrays.stream(stringInput.get(0).split(","))
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(Collectors.toList());

        stringInput.add("");
        
        List<BingoBoard> bingoBoards = new ArrayList<>();
        List<String> bingoBoard = new ArrayList<>();
        for (int line = 1; line < stringInput.size(); line++) {
            if (stringInput.get(line).matches(" *")) {
                if (!bingoBoard.isEmpty()) {
                    bingoBoards.add(BingoBoard.parse(bingoBoard));
                }
                bingoBoard.clear();
            } else {
                bingoBoard.add(stringInput.get(line));
            }
        }

        BingoBoard firstWinningBoard = null;
        Integer firstWinningNumber = null;

        BingoBoard lastWinningBoard = null;
        Integer lastWinningNumber = null;

        for (Integer n: drawnNumbers) {

            for (BingoBoard bb : bingoBoards) {
                bb.applyDrawnNumber(n);
            }

            List<BingoBoard> wonBoards = bingoBoards.stream()
                    .filter(BingoBoard::wins)
                    .collect(Collectors.toList());

            if (!wonBoards.isEmpty()) {
                bingoBoards.removeAll(wonBoards);

                if (firstWinningBoard == null) {
                    firstWinningBoard = wonBoards.get(0);
                    firstWinningNumber = n;
                }

                lastWinningBoard = wonBoards.get(0);
                lastWinningNumber = n;
            }


        }

        System.out.println("Result Part 1: "+(firstWinningBoard.getScore() * firstWinningNumber));


        
        System.out.println("Result Part 2: "+(lastWinningBoard.getScore() * lastWinningNumber));

    }

    private static class BingoBoard {

        private int[][] board;
        private boolean[][] match;
        private int[] matchedNrInRow;
        private int[] matchedNrInCol;

        public BingoBoard(int size) {
            this.board = new int[size][size];
            this.match = new boolean[size][size];
            this.matchedNrInRow = new int[size];
            this.matchedNrInCol = new int[size];
        }

        public void applyDrawnNumber(Integer drawnNumber) {
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (board[r][c] == drawnNumber) {
                        if (!match[r][c]) {
                            match[r][c] = true;
                            matchedNrInCol[c]++;
                            matchedNrInRow[r]++;
                        }
                    }
                }
            }
        }

        public boolean wins() {
            for (int i : matchedNrInRow) {
                if (i == matchedNrInRow.length) {
                    return true;
                }
            }

            for (int i : matchedNrInCol) {
                if (i == matchedNrInCol.length) {
                    return true;
                }
            }

            return false;
        }

        public int getScore() {
            int score = 0;
            for (int r = 0; r < board.length; r++) {
                for (int c = 0; c < board[r].length; c++) {
                    if (!match[r][c]) {
                        score += board[r][c];
                    }
                }
            }
            return score;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int[] row : board) {
                sb.append(Arrays.toString(row));
            }
            return sb.toString();
        }

        public static BingoBoard parse(List<String> strings) {
            BingoBoard bingoBoard = new BingoBoard(strings.size());

            for (int line = 0; line < strings.size(); line++) {
                int[] x = Arrays.stream(strings.get(line).trim().split(" +"))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                System.arraycopy(x, 0, bingoBoard.board[line], 0, x.length);
            }

            return bingoBoard;
        }
    }
}
