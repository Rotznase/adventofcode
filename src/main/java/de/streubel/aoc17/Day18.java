package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.*;

public class Day18 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {

        // Teil 1
        Program program = new Program(0, 1, true);
        program.run();
        System.out.println(program.lastSound);

        // Teil 2
        Program p0 = new Program(0, 2, true);
        Program p1 = new Program(1, 2, false);

        p0.other = p1;
        p1.other = p0;

        try {
            if (p0.master) {
                p0.run();
            } else if (p1.master) {
                p1.run();
            }
        } catch (RuntimeException e) {
            if (!e.getMessage().equals("Deadlock detected"))
                throw e;
        }

        System.out.println(p1.messageCounter);
    }

    private class Program implements Runnable {

        long programId;
        long lastSound;
        int mode;

        Queue<Long> queue;
        Program other;

        long messageCounter;
        Map<Character, Long> registerValues;

        boolean master;
        int i=0;

        Program(long programId, int mode, boolean master) {
            this.programId = programId;
            this.queue = new LinkedList<>();
            this.other = null;
            this.messageCounter = 0;
            this.mode = mode;
            this.master = master;

            registerValues = new HashMap<>();
            if (mode == 2) {
                registerValues.put('p', programId);
            }
        }

        @Override
        public void run() {
            char register;
            boolean stop = false;
            while (!stop) {
                String instruction = input[i];
                String cmd = instruction.substring(0, 3);
                switch (cmd) {
                    case "set":
                        long[]  args = getOpArgs(instruction, registerValues);
                        register = instruction.charAt(4);
                        registerValues.put(register, args[1]);
                        i++;
                        break;
                    case "add":
                        args = getOpArgs(instruction, registerValues);
                        register = instruction.charAt(4);
                        registerValues.put(register, args[0] + args[1]);
                        i++;
                        break;
                    case "mul":
                        args = getOpArgs(instruction, registerValues);
                        register = instruction.charAt(4);
                        registerValues.put(register, args[0] * args[1]);
                        i++;
                        break;
                    case "mod":
                        args = getOpArgs(instruction, registerValues);
                        register = instruction.charAt(4);
                        registerValues.put(register, args[0] % args[1]);
                        i++;
                        break;
                    case "jgz":
                        args = getOpArgs(instruction, registerValues);
                        i += args[0] > 0 ? args[1] : 1;
                        break;
                    case "snd":
                        args = getOpArgs(instruction, registerValues);
                        lastSound = args[0];
                        if (mode == 2) {
                            other.queue.add(lastSound);
                            messageCounter++;
                        }
                        i++;
                        break;
                    case "rcv":
                        register = instruction.charAt(4);

                        if (mode == 1) {
                            args = getOpArgs(instruction, registerValues);
                            if (args[0] != 0) {
                                registerValues.put(register, lastSound);
                                stop = true;
                            }
                        }
                        if (mode == 2) {
                            if (!this.queue.isEmpty()) {
                                registerValues.put(register, this.queue.poll());
                            } else {
                                if (master) {
                                    other.run();
                                    if (!this.queue.isEmpty()) {
                                        registerValues.put(register, this.queue.poll());
                                    } else {
                                        throw new RuntimeException("Deadlock detected");
                                    }
                                } else {
                                    return;
                                }
                            }
                        }
                        i++;
                        break;
                }

                if (i >= input.length) {
                    stop = true;
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
//            "set a 1",
//            "add a 2",
//            "mul a a",
//            "mod a 5",
//            "snd a",
//            "set a 0",
//            "rcv a",
//            "jgz a -1",
//            "set a 1",
//            "jgz a -2",

//            "snd 1",
//            "snd 2",
//            "snd p",
//            "rcv a",
//            "rcv b",
//            "rcv c",
//            "rcv d",

            "set i 31",
            "set a 1",
            "mul p 17",
            "jgz p p",
            "mul a 2",
            "add i -1",
            "jgz i -2",
            "add a -1",
            "set i 127",
            "set p 622",
            "mul p 8505",
            "mod p a",
            "mul p 129749",
            "add p 12345",
            "mod p a",
            "set b p",
            "mod b 10000",
            "snd b",
            "add i -1",
            "jgz i -9",
            "jgz a 3",
            "rcv b",
            "jgz b -1",
            "set f 0",
            "set i 126",
            "rcv a",
            "rcv b",
            "set p a",
            "mul p -1",
            "add p b",
            "jgz p 4",
            "snd a",
            "set a b",
            "jgz 1 3",
            "snd b",
            "set f 1",
            "add i -1",
            "jgz i -11",
            "snd a",
            "jgz f -16",
            "jgz a -19",
    };
 }
