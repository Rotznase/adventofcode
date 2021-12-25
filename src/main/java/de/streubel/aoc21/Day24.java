package de.streubel.aoc21;

import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Integer.parseInt;
import static org.apache.commons.lang3.StringUtils.substringAfter;


public class Day24 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {


        int[] a = stringInput.stream()                                                              // inp w
                .filter(instruction -> instruction.matches("div z -*\\d+"))                   // mul x 0
                .mapToInt(instruction -> parseInt(substringAfter(instruction, "div z ")))  // add x z
                .toArray();                                                                         // mod x 26
                                                                                                    // div z a <===
        int[] b = stringInput.stream()                                                              // add x b <===
                .filter(instruction -> instruction.matches("add x -*\\d+"))                   // eql x w
                .mapToInt(instruction -> parseInt(substringAfter(instruction, "add x ")))  // eql x 0
                .toArray();                                                                         // mul y 0
                                                                                                    // add y 25
        int[] cc = stringInput.stream()                                                             // mul y x
                .filter(instruction -> instruction.matches("add y -*\\d+"))                   // add y 1
                .mapToInt(instruction -> parseInt(substringAfter(instruction, "add y ")))  // mul z y
                .toArray();                                                                         // mul y 0
                                                                                                    // add y w
        int[] c = IntStream.range(0, cc.length)                                                     // add y c <===
                .filter(n -> n % 3 == 2)                                                            // mul y x
                .map(index -> cc[index])                                                            // add z y
                .toArray();

        List<Long> validModelNumbers = findValidModelNumbers(0, 0, a, b, c, 0);

        long maxValidModelNumber = validModelNumbers.stream().max(Long::compareTo).get();
        long minValidModelNumber = validModelNumbers.stream().min(Long::compareTo).get();

        System.out.println("Result Part 1: "+maxValidModelNumber);
        System.out.println("Result Part 2: "+minValidModelNumber);
    }

    private static boolean canZeroBeReached(long z, int[] aSoFar) {
        for (int a : aSoFar) {
            z /= a;
            if (z == 0) {
                return true;
            }
        }
        return false;
    }

    private static long x(int w, long z, int a, int b, int c) {
        if (z % 26 + b != w) {
            z = z * 26 / a + w + c;
        } else {
            z = z / a;
        }

        return z;
    }

    private static List<Long> findValidModelNumbers(long modelNrSoFar, long z, int[] a, int[] b, int[] c, int level) {

        List<Long> validModelNumbers = new ArrayList<>();

        for (int w = 9; w >= 1; w--) {

            long _modelNrSoFar = modelNrSoFar * 10 + w;
            long _z = x(w, z, a[level], b[level], c[level]);

            if (level == 13) {
                if (_z == 0) {
                    validModelNumbers.add(_modelNrSoFar);
                }
            } else if (canZeroBeReached(_z, Arrays.copyOfRange(a, level + 1, a.length))) {
                validModelNumbers.addAll(findValidModelNumbers(_modelNrSoFar, _z, a, b, c, level + 1));
            }
        }

        return validModelNumbers;
    }

//    private static Map<String, Long> execute(List<String> aluProgram, long input) {
//
//        Map<String, Long> register = new HashMap<>();
//
//        Iterator<Integer> iter = Long.toString(input).chars().iterator();
//
//        for (String instr : aluProgram) {
//            final String[] instrParts = instr.split(" ");
//            long a;
//            long b;
//            switch (instrParts[0]) {
//                case "inp":
//                    register.put(instrParts[1], (long) (iter.next() - '0'));
//                    break;
//                case "add":
//                    a = register.getOrDefault(instrParts[1], 0L);
//                    b = parseSecondInstrArg(instrParts[2], register);
//                    register.put(instrParts[1], (long) a + b);
//                    break;
//                case "mul":
//                    a = register.getOrDefault(instrParts[1], 0L);
//                    b = parseSecondInstrArg(instrParts[2], register);
//                    register.put(instrParts[1], (long) a * b);
//                    break;
//                case "div":
//                    a = register.getOrDefault(instrParts[1], 0L);
//                    b = parseSecondInstrArg(instrParts[2], register);
//                    register.put(instrParts[1], (long) a / b);
//                    break;
//                case "mod":
//                    a = register.getOrDefault(instrParts[1], 0L);
//                    b = parseSecondInstrArg(instrParts[2], register);
//                    register.put(instrParts[1], (long) a % b);
//                    break;
//                case "eql":
//                    a = register.getOrDefault(instrParts[1], 0L);
//                    b = parseSecondInstrArg(instrParts[2], register);
//                    register.put(instrParts[1], a == b ? 1L : 0L);
//                    break;
//            }
//        }
//        System.out.println(register.get("z"));
//
//        return register;
//    }
//
//    private static long parseSecondInstrArg(String secondInstrArg, Map<String, Long> register) {
//        long b = 0;
//        if (secondInstrArg.matches("-*[0-9]+")) {
//            b = parseInt(secondInstrArg);
//        } else if (secondInstrArg.matches("[w-z]")) {
//            b = register.getOrDefault(secondInstrArg, 0L);
//        }
//        return b;
//    }

}
