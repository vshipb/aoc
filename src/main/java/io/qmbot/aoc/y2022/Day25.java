package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Day25 implements Puzzle {
    @Override
    public String part1(String input) {
        return result1(input.split("\n"));
    }

    static String result1(String[] parseInput) {
        int length = parseInput[0].length();
        List<Character> result = new ArrayList<>();
        StringBuilder r = new StringBuilder();
        while (result.size() < length) {
            result.add('0');
        }
        for (String string : parseInput) {
            plus(result, string);
        }
        for (int i = result.size() - 1; i >= 0; i--) {
            r.append(result.get(i));
        }
        return String.valueOf(r);
    }

    static void plus(List<Character> result, String string) {
        for (int i = string.length() - 1, j = 0; i >= 0; i--, j++) {
            if (j == result.size()) result.add('0');
            Queue<Character> plus = plusChar(plusInt(result.get(j), string.charAt(i)));
            int n = j;
            while (!plus.isEmpty()) {
                char nextChar = plus.poll();
                result.set(n, nextChar);
                if (!plus.isEmpty()) {
                    n++;
                    if (n == result.size()) result.add('0');
                    plus = plusChar(plusInt(result.get(n), plus.peek()));
                }
            }
        }
    }

    static int plusInt(char first, char second) {
        int one = decimalChar(first);
        int two = decimalChar(second);
        return one + two;
    }

    static Queue<Character> plusChar(int number) {
        Queue<Character> list = new LinkedList<>();
        switch (number) {
            case -4 -> list.addAll(List.of('1', '-'));
            case -3 -> list.addAll(List.of('2', '-'));
            case -2 -> list.add('=');
            case -1 -> list.add('-');
            case 0 -> list.add('0');
            case 1 -> list.add('1');
            case 2 -> list.add('2');
            case 3 -> list.addAll(List.of('=', '1'));
            case 4 -> list.addAll(List.of('-', '1'));
            default -> System.out.println("Wrong char");
        }
        return list;
    }

    static int decimalChar(char c) {
        switch (c) {
            case '=':
                return -2;
            case '-':
                return -1;
            case '0', '1', '2':
                return Character.getNumericValue(c);
            default:
                System.out.println("Wrong char");
                return 0;
        }
    }

    @Override
    public String part2(String input) {
        return null;
    }
}
