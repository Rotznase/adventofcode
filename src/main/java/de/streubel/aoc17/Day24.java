package de.streubel.aoc17;

import de.streubel.AdventOfCodeRunner;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;

public class Day24 extends AdventOfCodeRunner {
    @Override
    public void run(List<String> stringInput) {

        Collection<String> s = new ArrayList<>(Arrays.asList(input));

        long maxStrengthPart1 = 0;
        long maxStrengthPart2 = 0;
        long maxDepthTotal = 0;
        for (String start: s.stream().filter(s1 -> s1.indexOf("0") == 0).collect(Collectors.toSet())) {
            Component root = new Component(start);
            s.remove(start);
            makeTree(root, s);

            Collection<Component> leafs = root.getLeafs();

            int x = leafs.stream().mapToInt(Component::getChainStrength).max().orElse(0);
            maxStrengthPart1 = max(maxStrengthPart1, x);

            int maxDepth = leafs.stream()
                    .mapToInt(Component::depth)
                    .max()
                    .orElse(0);
            if (maxDepth >= maxDepthTotal) {
                maxDepthTotal = maxDepth;
                x = leafs.stream()
                        .filter(component -> component.depth() == maxDepth)
                        .mapToInt(Component::getChainStrength)
                        .max()
                        .orElse(0);
                maxStrengthPart2 = x;
            }
        }

        System.out.println("Part 1: strongest = "+maxStrengthPart1);
        System.out.println("Part 2: strongest = "+maxStrengthPart2);


    }

    private void makeTree(Component node, Collection<String> remainingInput) {
        for (String line: remainingInput) {
            Component c = new Component(line);
            if (node.type2 == c.type1 || node.type2 == c.type2) {
                if (node.type2 == c.type2)
                    c.swichtType();
                node.add(c);
                final ArrayList<String> strings = new ArrayList<>(remainingInput);
                strings.remove(line);
                makeTree(c, strings);
            }
        }
    }

    class Component {
        String orig;
        int type1;
        int type2;

        Component parent;
        List<Component> children;

        Component(String line) {
            this.orig = line;

            final String[] compTypes = line.split("/");
            this.type1 = Integer.parseInt(compTypes[0]);
            this.type2 = Integer.parseInt(compTypes[1]);

            parent = null;
            children = new ArrayList<>();
        }

        Component add(Component child) {
            children.add(child);
            child.parent = this;
            return child;
        }

        Collection<Component> getLeafs() {
            final ArrayList<Component> leafs = new ArrayList<>();
            leafs.add(this);
            for (Component child: children) {
                leafs.addAll(child.getLeafs());
            }
            return leafs;
        }

        void swichtType() {
            int temp = type1;
            type1 = type2;
            type2 = temp;
        }

        int getStrength() {
            return type1 + type2;
        }

        int getChainStrength() {
            int strength = this.getStrength();
            Component parent = this.parent;
            while (parent != null) {
                strength += parent.getStrength();
                parent = parent.parent;
            }
            return strength;
        }

        int depth() {
            int depth = 1;
            Component parent = this.parent;
            while (parent != null) {
                depth ++;
                parent = parent.parent;
            }
            return depth;
        }

        void print(final Writer writer) throws IOException {
            print(writer, "", true);
        }

        void print(final Writer writer, final String prefix, final boolean isTail) throws IOException {
            writer.write(prefix + (isTail ? "`-- " : "|-- ") + orig + "\n");
            for (int i = 0; i < children.size(); i++)
                children.get(i).print(writer, prefix + (isTail ? "    " : "|   "), i == children.size()-1);
        }

        @Override
        public String toString() {
            StringWriter w = new StringWriter();
            try {
                print(w);
            } catch (IOException e) {
                // StringWriter wirft keine IOException.
            }
            return w.toString();
        }

    }

    private String[] input = {
//        "0/2",
//        "2/2",
//        "2/3",
//        "3/4",
//        "3/5",
//        "0/1",
//        "10/1",
//        "9/10",
        "14/42",
        "2/3",
        "6/44",
        "4/10",
        "23/49",
        "35/39",
        "46/46",
        "5/29",
        "13/20",
        "33/9",
        "24/50",
        "0/30",
        "9/10",
        "41/44",
        "35/50",
        "44/50",
        "5/11",
        "21/24",
        "7/39",
        "46/31",
        "38/38",
        "22/26",
        "8/9",
        "16/4",
        "23/39",
        "26/5",
        "40/40",
        "29/29",
        "5/20",
        "3/32",
        "42/11",
        "16/14",
        "27/49",
        "36/20",
        "18/39",
        "49/41",
        "16/6",
        "24/46",
        "44/48",
        "36/4",
        "6/6",
        "13/6",
        "42/12",
        "29/41",
        "39/39",
        "9/3",
        "30/2",
        "25/20",
        "15/6",
        "15/23",
        "28/40",
        "8/7",
        "26/23",
        "48/10",
        "28/28",
        "2/13",
        "48/14",
    };

}
