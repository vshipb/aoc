package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;

public class Day03 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).mapToInt(string -> priority(sameItem(string))).sum();
    }

    @Override
    public Integer part2(String input) {
        int sum = 0;
        String[] strings = input.split(REGEX_NEW_LINE);
        for (int i = 0; i < strings.length; i = i + 3) {
            sum += priority(sameItem(strings[i], strings[i + 1], strings[i + 2]));
        }
        return sum;
    }

    private static char sameItem(String string) {
        int half = string.length() / 2;
        for (int i = 0; i < half; i++) {
            char c = string.charAt(i);
            if (string.lastIndexOf(c) >= half) {
                return (c);
            }
        }
        throw new IllegalArgumentException();
    }

    private static char sameItem(String one, String two, String three) {
        for (int i = 0; i < one.length(); i++) {
            char c = one.charAt(i);
            if (two.indexOf(c) > -1 & (three.indexOf(c) > -1)) {
                return c;
            }
        }
        throw new IllegalArgumentException();
    }

    private static int priority(char c) {
        return c > 96 ? c - 96 : c - 64 + 26;
    }
}
