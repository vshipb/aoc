package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;

public class Day11 implements Puzzle {
    @Override
    public String part1(String input) {
        List<Monkey> monkeys = monkeys(input);
        turns(monkeys, 3, 20);
        return String.valueOf(business(monkeys));
    }

    @Override
    public String part2(String input) {
        List<Monkey> monkeys = monkeys(input);
        long divisible = monkeys.stream().mapToLong(monkey -> monkey.divisible).reduce(1, (a, b) -> a * b);
        turns(monkeys, divisible, 10000);
        return String.valueOf(business(monkeys));
    }

    static long business(List<Monkey> monkeys) {
        long[] business = monkeys.stream().mapToLong(monkey -> monkey.times).sorted().toArray();
        int size = monkeys.size();
        return business[size - 1] * business[size - 2];
    }

    void turns(List<Monkey> monkeys, long divisible, int rounds) {
        for (int i = 0; i < rounds; i++) {
            for (Monkey monkey : monkeys) {
                monkey.turn(monkeys, divisible);
            }
        }
    }

    static List<Monkey> monkeys(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
        List<Monkey> monkeys = new ArrayList<>();
        for (int i = 0; i < strings.size(); i = i + 7) {
            List<String> bluePrint = strings.subList(i, i + 6);
            monkeys.add(new Monkey(bluePrint));
        }
        return monkeys;
    }

    private static class Monkey {
        List<Long> items = new ArrayList<>();
        char operation;
        String change;
        Long divisible;
        int ifTrue;
        int ifFalse;
        long times;

        public Monkey(List<String> bluePrint) {
            for (int j = 4; j < bluePrint.get(1).split(" ").length; j++) {
                this.items.add(Long.valueOf(bluePrint.get(1).split(" ")[j].replaceAll(",", "")));
            }
            this.operation = bluePrint.get(2).split(" ")[6].charAt(0);
            this.change = bluePrint.get(2).split(" ")[7];
            this.divisible = Long.valueOf(bluePrint.get(3).split(" ")[5]);
            this.ifTrue = Integer.parseInt(bluePrint.get(4).split(" ")[9]);
            this.ifFalse = Integer.parseInt(bluePrint.get(5).split(" ")[9]);
            this.times = 0;
        }

        private long operation(long value) {
            times++;
            if (change.equals("old")) return value * value;
            return (operation == '*') ? value * Integer.parseInt(change) : value + Integer.parseInt(change);
        }

        private boolean test(long value) {
            return value % divisible == 0;
        }

        void turn(List<Monkey> monkeys, long divisible) {
            items.forEach(item -> {
                long newValue = newValue(divisible, operation(item));
                monkeys.get(test(newValue) ? ifTrue : ifFalse).items.add(newValue);
            });
            items.clear();
        }

        long newValue(long divisible, long newValue) {
            return (divisible == 3) ? newValue / 3 : newValue % divisible;
        }
    }
}

