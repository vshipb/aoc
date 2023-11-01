package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongBinaryOperator;

public class Day11 implements Puzzle {
    @Override
    public Long part1(String input) {
        List<Monkey> monkeys = monkeys(input);
        turns(monkeys, 3, 20);
        return business(monkeys);
    }

    @Override
    public Long part2(String input) {
        List<Monkey> monkeys = monkeys(input);
        int divisor = monkeys.stream().mapToInt(monkey -> monkey.divisibleBy).reduce(1, Math::multiplyExact);
        turns(monkeys, divisor, 10000);
        return business(monkeys);
    }

    static long business(List<Monkey> monkeys) {
        long[] business = monkeys.stream().mapToLong(monkey -> monkey.inspectedItemsCount).sorted().toArray();
        int size = monkeys.size();
        return business[size - 1] * business[size - 2];
    }

    void turns(List<Monkey> monkeys, int divisible, int rounds) {
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
            List<String> blueprint = strings.subList(i, i + 6);
            monkeys.add(new Monkey(blueprint));
        }
        return monkeys;
    }

    private static class Monkey {
        final List<Long> items = new ArrayList<>();
        final LongBinaryOperator operator;
        final Integer secondOperand;
        final int divisibleBy;
        final int ifTrue;
        final int ifFalse;
        int inspectedItemsCount;

        public Monkey(List<String> blueprint) {
            for (int j = 4; j < blueprint.get(1).split(" ").length; j++) {
                this.items.add(Long.valueOf(blueprint.get(1).split(" ")[j].replaceAll(",", "")));
            }

            this.operator = blueprint.get(2).split(" ")[6].charAt(0) == '*' ? Math::multiplyExact : Math::addExact;
            String secondOperand = blueprint.get(2).split(" ")[7];
            this.secondOperand = (secondOperand.equals("old")) ? null : Integer.parseInt(secondOperand);
            this.divisibleBy = Integer.parseInt(blueprint.get(3).split(" ")[5]);
            this.ifTrue = Integer.parseInt(blueprint.get(4).split(" ")[9]);
            this.ifFalse = Integer.parseInt(blueprint.get(5).split(" ")[9]);
            this.inspectedItemsCount = 0;
        }

        private long operation(long firstOperand) {
            inspectedItemsCount++;
            return operator.applyAsLong(firstOperand, secondOperand != null ? secondOperand : firstOperand);
        }

        private boolean test(long value) {
            return value % divisibleBy == 0;
        }

        void turn(List<Monkey> monkeys, int divisor) {
            items.forEach(item -> {
                long newValue = newValue(divisor, operation(item));
                monkeys.get(test(newValue) ? ifTrue : ifFalse).items.add(newValue);
            });
            items.clear();
        }

        long newValue(int divisor, long newValue) {
            return (divisor == 3) ? newValue / 3 : newValue % divisor;
        }
    }
}

