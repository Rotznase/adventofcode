package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day25 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {

        Turing machine = new Turing();
        Tape tape = new Tape();
        for (int i=0; i<12173597; i++) {
            machine.nextStep(tape);
        }

        System.out.println(tape.medium.values().stream().filter(character -> character=='1').count());
    }

    class Turing {
        State currentState;
        int position;

        Turing() {
            currentState = State.A;
            position = 0;
        }

        void nextStep(Tape tape) {
            switch (currentState) {
                case A:
                    if (tape.get(position) == '0') {
                        tape.set(position, '1');
                        position++;
                        currentState = State.B;
                    } else if (tape.get(position) == '1') {
                        tape.set(position, '0');
                        position--;
                        currentState = State.C;
                    }
                    break;

                case B:
                    if (tape.get(position) == '0') {
                        tape.set(position, '1');
                        position--;
                        currentState = State.A;
                    } else if (tape.get(position) == '1') {
                        tape.set(position, '1');
                        position++;
                        currentState = State.D;
                    }
                    break;

                case C:
                    if (tape.get(position) == '0') {
                        tape.set(position, '1');
                        position++;
                        currentState = State.A;
                    } else if (tape.get(position) == '1') {
                        tape.set(position, '0');
                        position--;
                        currentState = State.E;
                    }
                    break;

                case D:
                    if (tape.get(position) == '0') {
                        tape.set(position, '1');
                        position++;
                        currentState = State.A;
                    } else if (tape.get(position) == '1') {
                        tape.set(position, '0');
                        position++;
                        currentState = State.B;
                    }
                    break;

                case E:
                    if (tape.get(position) == '0') {
                        tape.set(position, '1');
                        position--;
                        currentState = State.F;
                    } else if (tape.get(position) == '1') {
                        tape.set(position, '1');
                        position--;
                        currentState = State.C;
                    }
                    break;

                case F:
                    if (tape.get(position) == '0') {
                        tape.set(position, '1');
                        position++;
                        currentState = State.D;
                    } else if (tape.get(position) == '1') {
                        tape.set(position, '1');
                        position++;
                        currentState = State.A;
                    }
                    break;
            }
        }
    }

    class Tape {
        Map<Integer, Character> medium;

        public Tape() {
            medium = new HashMap<>();
        }

        char get(int position) {
            return medium.getOrDefault(position, '0');
        }

        void set(int position, char c) {
            medium.put(position, c);
        }

    }

    enum State {A, B, C, D, E, F}

    /**
Begin in state A.
Perform a diagnostic checksum after 12173597 steps.

In state A:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state B.
  If the current value is 1:
    - Write the value 0.
    - Move one slot to the left.
    - Continue with state C.

In state B:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the left.
    - Continue with state A.
  If the current value is 1:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state D.

In state C:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state A.
  If the current value is 1:
    - Write the value 0.
    - Move one slot to the left.
    - Continue with state E.

In state D:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state A.
  If the current value is 1:
    - Write the value 0.
    - Move one slot to the right.
    - Continue with state B.

In state E:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the left.
    - Continue with state F.
  If the current value is 1:
    - Write the value 1.
    - Move one slot to the left.
    - Continue with state C.

In state F:
  If the current value is 0:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state D.
  If the current value is 1:
    - Write the value 1.
    - Move one slot to the right.
    - Continue with state A.
     */
}
