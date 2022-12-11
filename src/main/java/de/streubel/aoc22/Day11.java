package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.substringAfter;

public class Day11 extends AdventOfCodeRunner {

    private static boolean insidePart1;
    private static int lcm;

    @Override
    public void run(List<String> stringInput) {

        long resultPart1 = 0;
        long resultPart2 = 0;

        for (int maxRounds : Arrays.asList(20, 10000)) {

            insidePart1 = maxRounds == 20;

            List<Monkey> monkeys = new ArrayList<>();
            for (int i = 0; i < stringInput.size(); i++) {
                if (stringInput.get(i).startsWith("Monkey")) {
                    final String monkeyName = stringInput.get(i);
                    monkeys.add(new Monkey(monkeyName, stringInput.subList(i+1, i+6)));
                    i += 6;
                }
            }

            lcm = monkeys.stream().map(Monkey::getDivisor).reduce(1, (o1, o2) -> o1*o2);

            for (int round = 0; round < maxRounds; round++) {
                for (Monkey monkey : monkeys) {
                    monkey.throwItems(monkeys);
                }
            }

            final List<Long> mostActiveMonkeys = monkeys.stream()
                    .map(Monkey::getInspections)
                    .sorted(Comparator.reverseOrder())
                    .limit(2)
                    .toList();

            if (maxRounds == 20) {
                resultPart1 = mostActiveMonkeys.get(0) * mostActiveMonkeys.get(1);
            }

            if (maxRounds == 10000) {
                resultPart2 = mostActiveMonkeys.get(0) * mostActiveMonkeys.get(1);
            }
        }

        System.out.println("Result Part 1 (51075): " + resultPart1);
        assert resultPart1 == 51075;

        System.out.println("Result Part 2 (11741456163): " + resultPart2);
        assert resultPart2 == 11741456163L;
    }

    private static class Monkey {

        private final String monkeyName;
        private final Deque<Item> items;
        private final String operation;
        private final int divisor;
        private final Map<Boolean, Integer> receiverMonkeys;
        private int inspections;


        public Monkey(String monkeyName, List<String> monkeyConfig) {
            this.monkeyName = monkeyName;
            items = Arrays
                    .stream(substringAfter(monkeyConfig.get(0), "Starting items:").trim().split(", "))
                    .map(Item::parseItem)
                    .collect(Collectors.toCollection(ArrayDeque::new));
            operation = substringAfter(monkeyConfig.get(1), "Operation:").trim();
            divisor = Integer.parseInt(substringAfter(monkeyConfig.get(2), "Test: divisible by ").trim());

            receiverMonkeys = new HashMap<>();
            Pattern pattern = Pattern.compile("If (true|false): throw to monkey (\\d)");

            Matcher matcher;

            matcher = pattern.matcher(monkeyConfig.get(3));
            matcher.find();
            receiverMonkeys.put(Boolean.parseBoolean(matcher.group(1)), Integer.parseInt(matcher.group(2)));

            matcher = pattern.matcher(monkeyConfig.get(4));
            matcher.find();
            receiverMonkeys.put(Boolean.parseBoolean(matcher.group(1)), Integer.parseInt(matcher.group(2)));

            inspections = 0;
        }

        public void throwItems(List<Monkey> monkeys) {
            Item item = items.poll();
            while (item != null) {
                item.inspect(operation);
                inspections++;
                if (insidePart1) {
                    item.setWorryLevel(item.getWorryLevel() / 3);
                }
                lcm = 9699690;
                item.setWorryLevel(item.getWorryLevel() % lcm);
                final Monkey receiverMonkey = findReceiverMonkey(monkeys, item.getWorryLevel());
                receiverMonkey.receive(item);
                item = items.poll();
            }
        }

        public int getDivisor() {
            return divisor;
        }

        public Monkey findReceiverMonkey(List<Monkey> monkeys, long worryLevel) {
            return monkeys.get(receiverMonkeys.get(worryLevel % divisor == 0));
        }

        public void receive(Item item) {
            items.addLast(item);
        }

        public long getInspections() {
            return inspections;
        }

        @Override
        public String toString() {
            return monkeyName;
        }
    }

    private static class Item {
        private final int name;
        private long worryLevel;

        public Item(int name) {
            this.name = name;
            this.worryLevel = name;
        }

        public int getName() {
            return name;
        }

        public long getWorryLevel() {
            return worryLevel;
        }

        public void setWorryLevel(long worryLevel) {
            this.worryLevel = worryLevel;
        }

        public static Item parseItem(String number) {
            return new Item(Integer.parseInt(number));
        }

        public void inspect(String operation) {
            final String[] s = substringAfter(operation, "new = ").split(" ");

            long newWorryLevel = parseArgument(worryLevel, s[0]);
            switch (s[1]) {
                case "*" -> newWorryLevel *= parseArgument(worryLevel, s[2]);
                case "+" -> newWorryLevel += parseArgument(worryLevel, s[2]);
            }

            worryLevel = newWorryLevel;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name=" + name +
                    ", worryLevel=" + worryLevel +
                    '}';
        }
    }

    private static long parseArgument(long worryLevel, String string) {
        long argument;
        if (string.indexOf("old") == 0) {
            argument = worryLevel;
        } else {
            argument = Integer.parseInt(string);
        }
        return argument;
    }

}
