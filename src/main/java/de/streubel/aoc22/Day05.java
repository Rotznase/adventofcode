package de.streubel.aoc22;

import com.google.common.collect.Lists;
import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;

public class Day05 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final List<Stack<Character>> stacksPart1 = parseStacks(stringInput);
        final List<Stack<Character>> stacksPart2 = SerializationUtils.clone((ArrayList<Stack<Character>>) stacksPart1);

        final List<MoveAction> actionList = parseActions(stringInput);


        for (MoveAction moveAction : actionList) {
            // Part 1
            for (int i = 0; i < moveAction.getNrOfCratesToMove(); i++) {
                final Character crate = stacksPart1.get(moveAction.getSrcStack() - 1).pop();
                stacksPart1.get(moveAction.getDstStack() - 1).push(crate);
            }

            // Part 2
            final List<Character> stack = new Stack<>();
            for (int i = 0; i < moveAction.getNrOfCratesToMove(); i++) {
                stack.add(stacksPart2.get(moveAction.getSrcStack() - 1).pop());
            }
            for (Character crate : Lists.reverse(stack)) {
                stacksPart2.get(moveAction.getDstStack() - 1).push(crate);
            }
        }

        final String resultPart1 = stacksPart1.stream().map(c -> "" + c.peek()).collect(joining());
        System.out.println("Result Part 1 (SPFMVDTZT): "+ resultPart1);

        final String resultPart2 = stacksPart2.stream().map(c -> "" + c.peek()).collect(joining());
        System.out.println("Result Part 2 (ZFSJBPRFP): "+ resultPart2);
    }

    private static List<MoveAction> parseActions(List<String> stringInput) {
        final Pattern movePattern = Pattern.compile("move (\\d+) from (\\d) to (\\d)");

        final List<MoveAction> actionList = new ArrayList<>();
        boolean inStackDrawing = true;
        for (String line : stringInput) {
            if (line.isEmpty()) {
                inStackDrawing = false;
            } else {
                if (!inStackDrawing) {
                    final Matcher matcher = movePattern.matcher(line);
                    if (matcher.find()) {
                        int nrOfCrates = Integer.parseInt(matcher.group(1));
                        int srcStack = Integer.parseInt(matcher.group(2));
                        int dstStack = Integer.parseInt(matcher.group(3));

                        final MoveAction moveAction = new MoveAction(nrOfCrates, srcStack, dstStack);
                        actionList.add(moveAction);
                    }
                }
            }
        }
        return actionList;
    }

    private static List<Stack<Character>> parseStacks(List<String> stringInput) {
        final List<List<Character>> stackDrawing = new ArrayList<>();
        for (String l : stringInput) {
            if (l.isEmpty()) {
                stackDrawing.remove(stackDrawing.size()-1);
                break;
            }

            final List<Character> x = new ArrayList<>();
            stackDrawing.add(x);
            for (int i = 0; i < l.length(); i++) {
                if (i % 4 == 1) {
                    x.add(l.charAt(i));
                }
            }
        }

        final List<Stack<Character>> stacks = new ArrayList<>();
        for (List<Character> y : Lists.reverse(stackDrawing)) {
            for (int i = 0; i < y.size(); i++) {
                final Stack<Character> e;
                if (i < stacks.size()) {
                    e = stacks.get(i);
                } else {
                    e = new Stack<>();
                    stacks.add(e);
                }

                if (y.get(i) != ' ') {
                    e.push(y.get(i));
                }
            }
        }

        return stacks;
    }

    private static class MoveAction {
        private final int nrOfCratesToMove;
        private final int srcStack;
        private final int dstStack;

        public MoveAction(int nrOfCratesToMove, int srcStack, int dstStack) {
            this.nrOfCratesToMove = nrOfCratesToMove;
            this.srcStack = srcStack;
            this.dstStack = dstStack;
        }

        public int getNrOfCratesToMove() {
            return nrOfCratesToMove;
        }

        public int getSrcStack() {
            return srcStack;
        }

        public int getDstStack() {
            return dstStack;
        }
    }

}
