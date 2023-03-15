package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day11 implements Puzzle {
    @Override
    public String part1(String input) {
        List<String> strings = List.of(input.split("\n"));
        List<Monkey> monkeys = new ArrayList<>();
        for (int i = 0; i < strings.size(); i = i + 7) {
            List<String> monkey = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                monkey.add(strings.get(i + j));
            }
            monkeys.add(parse(monkey));
        }
        for (int i = 0; i < 20; i++) {
            for (Monkey monkey : monkeys) {
                monkey.turn(monkeys);
            }
        }
        long[] business = new long[monkeys.size()];
        for (int i = 0; i < monkeys.size(); i++) {
            business[i] = monkeys.get(i).times;
        }
        Arrays.sort(business);
        return Long.toString(business[monkeys.size() - 1] * business[monkeys.size() - 2]);
    }

    @Override
    public String part2(String input) {
        List<String> strings = List.of(input.split("\n"));
        List<Monkey> monkeys = new ArrayList<>();
        for (int i = 0; i < strings.size(); i = i + 7) {
            List<String> monkey = new ArrayList<>();
            for (int j = 0; j < 6; j++) {
                monkey.add(strings.get(i + j));
            }
            monkeys.add(parse(monkey));
        }
        long devisible = 1;
        for (Monkey monkey : monkeys) {
            devisible = devisible * monkey.divisible;
        }
        for (int i = 0; i < 10000; i++) {
            for (Monkey monkey : monkeys) {
                monkey.turn2(monkeys, devisible);
            }
        }
        long[] bussines = new long[monkeys.size()];
        for (int i = 0; i < monkeys.size(); i++) {
            bussines[i] = monkeys.get(i).times;
        }

        Arrays.sort(bussines);
        return Long.toString(bussines[monkeys.size() - 1] * bussines[monkeys.size() - 2]);
    }

    static Monkey parse(List<String> monkey) {
        Monkey result = new Monkey();
        for (String s : monkey) {
            String[] word = s.split(" ");
            if (word.length > 2) {
                if (word[2].equals("Starting")) {
                    for (int j = 4; j < s.split(" ").length; j++) {
                        result.items.add(Long.valueOf(word[j].replaceAll(",", "")));
                    }
                }
                if (word[2].equals("Operation:")) {
                    result.operation = word[6];
                    result.change = word[7];
                }
                if (word[2].equals("Test:")) {
                    result.divisible = Long.valueOf(word[5]);
                }
                if (word.length > 5 && word[5].equals("true:")) {
                    result.ifTrue = Integer.parseInt(word[9]);
                }
                if (word.length > 5 && word[5].equals("false:")) {
                    result.ifFalse = Integer.parseInt(word[9]);
                }
                result.times = 0;
            }
        }
        return result;
    }

    private static class Monkey {
        List<Long> items = new ArrayList<>();
        String operation;
        String change;
        Long divisible;
        int ifTrue;
        int ifFalse;
        long times;

        private long operation(long value) {
            times++;
            long newWorry = 0;
            if (change.equals("old")) {
                return value * value;
            }
            switch (operation) {
                case "*" -> newWorry = value * Integer.parseInt(change);
                case "+" -> newWorry = value + Integer.parseInt(change);
                default -> System.out.println("Недопустимая динамика");
            }
            return newWorry;
        }

        private boolean test(long value) {
            return value % divisible == 0;
        }

        void turn(List<Monkey> monkeys) {
            for (long item : items) {
                long newValue = operation(item);
                newValue = newValue / 3;
                if (test(newValue)) {
                    monkeys.get(ifTrue).items.add(newValue);
                } else {
                    monkeys.get(ifFalse).items.add(newValue);
                }
            }
            items.removeAll(items);
        }

        void turn2(List<Monkey> monkeys, long divisible) {
            for (long item : items) {
                long newValue = operation(item);
                newValue = newValue % divisible;
                if (test(newValue)) {
                    monkeys.get(ifTrue).items.add(newValue);
                } else {
                    monkeys.get(ifFalse).items.add(newValue);
                }
            }
            items.removeAll(items);
        }
    }
}
