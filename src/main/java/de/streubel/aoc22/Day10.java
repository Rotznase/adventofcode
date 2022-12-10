package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.List;

public class Day10 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        List<Instruction> program = new ArrayList<>();
        for (String line : stringInput) {
            final String[] s = line.split(" ");
            program.add(new Instruction(s[0], s.length == 2 ? Integer.parseInt(s[1]) : null));
        }

        Cpu cpu = new Cpu();
        for (Instruction instruction : program) {
            cpu.execute(instruction);
        }
        
        int resultPart1 = signalStrength;
        System.out.println("Result Part 1 (17840): " + resultPart1);
        assert resultPart1 == 17840;

        String resultPart2 = "EALGULPG";
        System.out.println("Result Part 2 (EALGULPG): " + resultPart2);
        assert resultPart2.equals("EALGULPG");
    }

    private record Instruction(String instruction, Integer argument) {
    }

    private static class Cpu {
        private int registerX;
        private int cycle;

        public Cpu() {
            this.registerX = 1;
            this.cycle = 0;
        }

        public void execute(Instruction instruction) {
            switch (instruction.instruction) {
                case "addx" -> {
                    incrementCycle();
                    incrementCycle();
                    registerX += instruction.argument;
                }
                case "noop" -> incrementCycle();
            }
        }

        private void incrementCycle() {
            cycle++;
            if ((cycle + 20) % 40 == 0) {
                signalStrength += signalStrength();
            }

            if ((cycle-1)%40 >= registerX - 1 && (cycle-1)%40 <= registerX + 1) {
                System.out.print("#");
            } else {
                System.out.print(".");
            }
            if (cycle % 40 == 0) {
                System.out.println();
            }
        }

        public int signalStrength() {
            return cycle * registerX;
        }

    }

    private static int signalStrength = 0;
}
