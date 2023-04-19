package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Day21 implements Puzzle {
    @Override
    public String part1(String input) {
        Map<String, Long> monkeyWithNumber = new HashMap<>();
        Map<String, String[]> monkeyWithOperation = new HashMap<>();

        for (String string : input.split("\n")) {
            String[] stringSplit = string.split(" ");
            String monkeyName = stringSplit[0].replaceFirst(":", "");
            if (stringSplit.length == 2) {
                monkeyWithNumber.put(monkeyName, Long.valueOf(stringSplit[1]));
            } else {
                monkeyWithOperation.put(monkeyName, Arrays.copyOfRange(stringSplit, 1, stringSplit.length));
            }
        }

        String firstMonkeyName;
        String secondMonkeyName;

        while (monkeyWithOperation.containsKey("root")) {
            for (Map.Entry<String, String[]> monkey : monkeyWithOperation.entrySet()) {
                firstMonkeyName = monkey.getValue()[0];
                secondMonkeyName = monkey.getValue()[2];

                if (monkeyWithNumber.containsKey(firstMonkeyName) && monkeyWithNumber.containsKey(secondMonkeyName)) {
                    monkeyWithNumber.put(monkey.getKey(), newNumberMonkeyValue(monkeyWithNumber.get(firstMonkeyName),
                            monkeyWithNumber.get(secondMonkeyName), monkey.getValue()[1]));
                    monkeyWithOperation.remove(monkey.getKey());

                    break;
                }
            }
        }

        return String.valueOf(monkeyWithNumber.get("root"));
    }

    @Override
    public String part2(String input) {
        Map<String, Long> monkeyWithNumber = new HashMap<>();
        Map<String, String[]> monkeyWithOperation = new HashMap<>();
        String firstMonkeyName;
        String secondMonkeyName;
        long low = -(long) Integer.MAX_VALUE * 1024 * 1024;
        long high = (long) Integer.MAX_VALUE * 1024 * 1024;
        long humn;

        while (true) {
            for (String string : input.split("\n")) {
                String[] stringSplit = string.split(" ");
                String monkeyName = stringSplit[0].replaceFirst(":", "");
                if (stringSplit.length == 2) {
                    monkeyWithNumber.put(monkeyName, Long.valueOf(stringSplit[1]));
                } else {
                    if (monkeyName.equals("root")) {
                        monkeyWithOperation.put(monkeyName, new String[]{stringSplit[1], "=", stringSplit[3]});
                    } else {
                        monkeyWithOperation.put(monkeyName, Arrays.copyOfRange(stringSplit, 1, stringSplit.length));
                    }
                }
            }

            monkeyWithNumber.remove("humn");

            humn = low + (high - low) / 2;

            monkeyWithNumber.put("humn", humn);

            while (monkeyWithOperation.containsKey("root")) {
                for (Map.Entry<String, String[]> monkey : monkeyWithOperation.entrySet()) {
                    firstMonkeyName = monkey.getValue()[0];
                    secondMonkeyName = monkey.getValue()[2];

                    if (monkeyWithNumber.containsKey(firstMonkeyName) && monkeyWithNumber.containsKey(secondMonkeyName)) {
                        monkeyWithNumber.put(monkey.getKey(), newNumberMonkeyValue(monkeyWithNumber.get(firstMonkeyName),
                                monkeyWithNumber.get(secondMonkeyName), monkey.getValue()[1]));
                        monkeyWithOperation.remove(monkey.getKey());

                        break;
                    }
                }
            }

            long root = monkeyWithNumber.get("root");

            if (0 < root) {
                // low = humn + 1;
                high = humn - 1;
            } else if (0 > root) {
                // high = humn - 1;
                low = humn + 1;
            } else {
                break;
            }

            monkeyWithNumber.clear();
            monkeyWithOperation.clear();
        }

        return String.valueOf(humn);
    }

    private static long newNumberMonkeyValue(long firstMonkeyValue, long secondMonkeyValue, String operation) {
        switch (operation) {
            case "-", "=" -> {
                return firstMonkeyValue - secondMonkeyValue;
            }
            case "+" -> {
                return firstMonkeyValue + secondMonkeyValue;
            }
            case "*" -> {
                return firstMonkeyValue * secondMonkeyValue;
            }
            case "/" -> {
                return firstMonkeyValue / secondMonkeyValue;
            }
            default -> throw new IllegalArgumentException("Operation " + operation + " is not valid");
        }
    }
}
