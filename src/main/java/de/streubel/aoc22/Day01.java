package de.streubel.aoc22;

import de.streubel.AdventOfCodeRunner;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class Day01 extends AdventOfCodeRunner {

    @Override
    public void run(List<String> stringInput) {

        final List<ElveCalories> elveCalories = new ArrayList<>();

        int elveNr = 0;
        elveCalories.add(new ElveCalories(""+elveNr));
        for (String calorieLine : stringInput) {
            if (calorieLine.isEmpty()) {
                elveCalories.add(new ElveCalories(""+(++elveNr)));
            } else {
                elveCalories.get(elveNr).addCalorie(Integer.parseInt(calorieLine));
            }
        }

        final Map<String, Integer> totalCalories = elveCalories.stream()
                .collect(
                        groupingBy(
                                ElveCalories::getElveName,
                                summingInt(ElveCalories::getTotalCalories)
                        )
                );

        int maxCalorie;

        maxCalorie = totalCalories.values().stream()
                .max(Integer::compareTo)
                .orElse(0);
        System.out.println("Result Part 1 (68923): "+ maxCalorie);

        maxCalorie = totalCalories.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .reduce(0, Integer::sum);
        System.out.println("Result Part 2 (200044): "+maxCalorie);

    }

    private static class ElveCalories {
        private final String elveName;
        private final List<Integer> calories;

        public ElveCalories(String elveName) {
            this.elveName = elveName;
            this.calories = new ArrayList<>();
        }

        public String getElveName() {
            return elveName;
        }

        public List<Integer> getCalories() {
            return calories;
        }

        public void addCalorie(final int calorie) {
            calories.add(calorie);
        }

        public int getTotalCalories() {
            return calories.stream().reduce(Integer::sum).orElse(0);
        }
    }
}
