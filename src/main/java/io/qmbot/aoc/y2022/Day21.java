package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.HashMap;
import java.util.Map;

public class Day21 implements Puzzle {
    @Override
    public String part1(String input) {
        Map<String, Monkey> monkeys = monkeys(input);
        return String.valueOf(monkeys.get("root").yell(monkeys));
    }

    @Override
    public String part2(String input) {
        long low = -(long) Integer.MAX_VALUE * 1024 * 1024;
        long high = (long) Integer.MAX_VALUE * 1024 * 1024;
        long humn;
        long root;

        Map<String, Monkey> monkeys = monkeys(input);
        ((MonkeyWithOperation) monkeys.get("root")).operation = "-";

        long lowValue = root(monkeys, low);
        long highValue = root(monkeys, high);

        if (Long.signum(lowValue) == Long.signum(highValue)) {
            throw new IllegalStateException();
        }

        while (true) {
            humn = low + (high - low) / 2;
            root = root(monkeys, humn);

            if (Long.signum(root) == Long.signum(lowValue)) {
                low = humn;
            } else if (Long.signum(root) == Long.signum(highValue)) {
                high = humn;
            } else {
                break;
            }
        }

        while (root(monkeys, humn) == 0) {
            humn--;
        }

        return String.valueOf(humn + 1);
    }

    private static Map<String, Monkey> monkeys(String input) {
        Map<String, Monkey> monkeys = new HashMap<>();
        for (String string : input.split("\n")) {
            String[] stringSplit = string.split(": ");
            String monkeyName = stringSplit[0];
            String[] value = stringSplit[1].split(" ");
            if (value.length == 1) {
                monkeys.put(monkeyName, new MonkeyWithNumber(Long.parseLong(value[0])));
            } else {
                monkeys.put(monkeyName, new MonkeyWithOperation(value[0], value[1], value[2]));
            }
        }
        return monkeys;
    }

    private static long root(Map<String, Monkey> monkeys, long humn) {
        ((MonkeyWithNumber) monkeys.get("humn")).number = humn;

        return monkeys.get("root").yell(monkeys);
    }

    interface Monkey {
        long yell(Map<String, Monkey> monkeys);
    }

    private static class MonkeyWithNumber implements Monkey {
        long number;

        public MonkeyWithNumber(long number) {
            this.number = number;
        }

        @Override
        public long yell(Map<String, Monkey> monkeys) {
            return number;
        }
    }

    private static class MonkeyWithOperation implements Monkey {
        String first;
        String second;
        String operation;

        public MonkeyWithOperation(String first, String operation, String second) {
            this.first = first;
            this.operation = operation;
            this.second = second;
        }

        @Override
        public long yell(Map<String, Monkey> monkeys) {
            switch (operation) {
                case "-" -> {
                    return monkeys.get(first).yell(monkeys) - monkeys.get(second).yell(monkeys);
                }
                case "+" -> {
                    return monkeys.get(first).yell(monkeys) + monkeys.get(second).yell(monkeys);
                }
                case "*" -> {
                    return monkeys.get(first).yell(monkeys) * monkeys.get(second).yell(monkeys);
                }
                case "/" -> {
                    return monkeys.get(first).yell(monkeys) / monkeys.get(second).yell(monkeys);
                }
                default -> throw new IllegalArgumentException("Operation " + operation + " is not valid");
            }
        }
    }
}