package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        List<Pair<Item, Item>> packetPairs = new ArrayList<>();

        Pair<Item, Item> pair = null;
        for (String line : stringInput) {
            if (line.isEmpty()) {
                continue;
            }
            List<Item> packet = parse(line.toCharArray());
            if (pair == null) {
                pair = MutablePair.of(packet.get(0), null);
            } else {
                pair.setValue(packet.get(0));
                packetPairs.add(pair);
                pair = null;
            }
        }

        int resultPart1 = 0;
        for (int i = 0; i < packetPairs.size(); i++) {
            Pair<Item, Item> itemPair = packetPairs.get(i);
            if (itemPair.getLeft().compareTo(itemPair.getRight()) <= 0) {
                resultPart1 += i+1;
            }
        }

        System.out.println("Result Part 1 (5340): " + resultPart1);
        assert resultPart1 == 5340;


        final Item dividerPacket1 = parse("[[2]]".toCharArray()).get(0);
        final Item dividerPacket2 = parse("[[6]]".toCharArray()).get(0);

        final List<Item> allPackets = new ArrayList<>();
        allPackets.add(dividerPacket1);
        allPackets.add(dividerPacket2);
        allPackets.addAll(packetPairs.stream().map(Pair::getLeft).toList());
        allPackets.addAll(packetPairs.stream().map(Pair::getRight).toList());

        final List<Item> sortedPackets = allPackets.stream()
                .sorted(Item::compareTo)
                .toList();

        final int indexOfDividerPacket1 = sortedPackets.indexOf(dividerPacket1);
        final int indexOfDividerPacket2 = sortedPackets.indexOf(dividerPacket2);
        final int decoderKey = (indexOfDividerPacket1 + 1) * (indexOfDividerPacket2 + 1);
        System.out.println("Result Part 2 (21276): " + decoderKey);
        assert decoderKey == 21276;
    }


    private interface Item extends Comparable<Item> {
        List<Item> getItems();
        void addAll(List<Item> items);
    }

    private static class PrimitiveItem implements Item {

        private final int number;

        public PrimitiveItem(String number) {
            this.number = Integer.parseInt(number);
        }

        @Override
        public List<Item> getItems() {
            return Collections.singletonList(this);
        }

        @Override
        public void addAll(List<Item> items) {
            throw new RuntimeException();
        }

        @Override
        public String toString() {
            return ""+number;
        }

        @Override
        public int compareTo(Item o) {
            if (o instanceof PrimitiveItem) {
                return this.number - ((PrimitiveItem) o).number;
            } else {
                final ComplexItem complexItem = new ComplexItem();
                complexItem.add(this);
                return complexItem.compareTo(o);
            }
        }
    }

    private static class ComplexItem implements Item {

        private final List<Item> items;

        public ComplexItem() {
            items = new ArrayList<>();
        }

        @Override
        public List<Item> getItems() {
            return items;
        }

        @Override
        public void addAll(List<Item> items) {
            this.items.addAll(items);
        }

        public void add(Item item) {
            this.items.add(item);
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("[");

            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                sb.append(item.toString());
                if (i < items.size()-1) {
                    sb.append(",");
                }
            }
            sb.append("]");

            return sb.toString();
        }

        @Override
        public int compareTo(Item o) {
            if (o instanceof PrimitiveItem) {
                ComplexItem item = new ComplexItem();
                item.add(o);
                return this.compareTo(item);
            } else {
                int commonIndex = Math.min(this.items.size(), o.getItems().size());
                int compareTo = 0;
                for (int i = 0; i < commonIndex; i++) {
                    compareTo = this.items.get(i).compareTo(o.getItems().get(i));
                    if (compareTo != 0) {
                        break;
                    }
                }

                if (compareTo == 0) {
                    compareTo = this.items.size() - o.getItems().size();
                }

                return compareTo;
            }
        }
    }

    private static List<Item> parse(char[] array) {
        List<Item> items = new ArrayList<>();
        for (int j = 0; j < array.length; j++) {

            if (array[j] == '[') {
                Item item = new ComplexItem();
                items.add(item);

                int startIndex = j+1;
                int endIndex = j;
                int bracketCounter = 0;
                for (; endIndex < array.length; endIndex++) {
                    switch (array[endIndex]) {
                        case '[' -> bracketCounter++;
                        case ']' -> bracketCounter--;
                    }

                    if (bracketCounter == 0) {
                        break;
                    }
                }

                if (bracketCounter == 0) {
                    final char[] subarray = ArrayUtils.subarray(array, startIndex, endIndex);
                    item.addAll(parse(subarray));
                    j = endIndex + 1;
                }

            } else if (Character.isDigit(array[j])) {
                StringBuilder sb = new StringBuilder();
                for (; j < array.length; j++) {
                    if (array[j] != ',') {
                        sb.append(array[j]);
                    } else {
                        break;
                    }
                }
                Item item = new PrimitiveItem(sb.toString());
                items.add(item);
            } else if (array[j] == ',') {
                ;
            }
        }

        return items;
    }

}
