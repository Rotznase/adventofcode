package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day23 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {
        Program program;

        // Teil 1
        program = new Program();
        program.run();
        System.out.println(program.mulCounter);

        // Teil 2
        System.out.println(part2(1));
    }

    private class Program implements Runnable {
        Map<Character, Long> registerValues;
        Map<Character, Long> registerDeltas;
        long mulCounter;

        Program() {
            registerValues = new HashMap<>();
            registerDeltas = new HashMap<>();
            mulCounter = 0;
        }

        @Override
        public void run() {
            int i=0;
            while (i < input.length) {
                String instruction = input[i];
                String cmd = instruction.substring(0, 3);
                long[] args = getOpArgs(instruction, registerValues);
                char register = instruction.charAt(4);

                switch (cmd) {
                    case "set":
                        registerValues.put(register, args[1]);
                        i++;
                        break;
                    case "sub":
                        registerValues.put(register, args[0] - args[1]);
                        i++;
                        break;
                    case "mul":
                        registerValues.put(register, args[0] * args[1]);
                        i++;
                        mulCounter++;
                        break;
                    case "jnz":
                        i += args[0] != 0 ? args[1] : 1;
                        break;
                }

            }
        }
    }

    private long[] getOpArgs(String instruction, Map<Character, Long> registerValues) {
        long [] args = new long[2];
        if (Character.isAlphabetic(instruction.charAt(4))) {
            char register = instruction.charAt(4);
            args[0] = registerValues.getOrDefault(register, 0L);
        } else {
            args[0] = Long.parseLong(""+instruction.charAt(4)); //todo this only scans 1-Digit Numbers!
        }
        if (instruction.length() > 6) {
            if (Character.isAlphabetic(instruction.charAt(6))) {
                args[1] = registerValues.getOrDefault(instruction.charAt(6), 0L);
            } else {
                args[1] = Integer.parseInt(instruction.substring(6));
            }
        }
        return args;
    }

    private String[] input = {
        "set b 65",
        "set c b",
        "jnz a 2",
        "jnz 1 5",
        "mul b 100",
        "sub b -100000",
        "set c b",
        "sub c -17000",
        "set f 1",
        "set d 2",
        "set e 2",
        "set g d",
        "mul g e",
        "sub g b",
        "jnz g 2",
        "set f 0",
        "sub e -1",
        "set g e",
        "sub g b",
        "jnz g -8",
        "sub d -1",
        "set g d",
        "sub g b",
        "jnz g -13",
        "jnz f 2",
        "sub h -1",
        "set g b",
        "sub g c",
        "jnz g 2",
        "jnz 1 3",
        "sub b -17",
        "jnz 1 -23",
    };

    long part2(int starta) {
        long a,b0,c,h;
        a = starta;
        b0 = 65;
        c = b0;
        h = 0;

        if (a != 0) {
            b0 = b0*100+100000;
            c = b0+17000;
        }

        for (long b=b0; b<=c; b+=17) {
            long f = 1;
            for (long d=2; d<b; d++) {
//                for (int e=2; e<b; e++) {
//                    if (d * e == b)
//                        f = 0;
//                }
                if (b/d >= 2 && b%d == 0)
                    f = 0;
            }

            if (f == 0) {
                h++;
            }
        }

        return h;
    }
//    long x(int starta) {
//        long a,b,c,d,e,f,g,h;
//        a = starta;
//        b = 65;
//        c = b;
//        d = 0;
//        e = 0;
//        f = 0;
//        g = 0;
//        h = 0;
//
//        if (a != 0) {
//            b = b*100+100000;
//            c = b+17000;
//        }
//
//        while (true) {
//            f = 1;
//            d = 2;
//
//            do {
//                e = 2;
//                do {
//                    g = d * e - b;
//                    if (g == 0)
//                        f = 0;
//                    e++;
//                    g = e - b;
//                } while (g != 0);
//
//                d++;
//                g = d - b;
//            } while (g != 0);
//
//            if (f == 0) {
//                h++;
//            }
//            g = b - c;
//            if (g == 0) {
//                break;
//            }
//            b = b + 17;
//        }
//
//        return h;
//    }
}
