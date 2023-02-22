package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day03 implements Puzzle {
    @Override
    public String part1(String input) {
        int sum = 0;

        for (String string : input.split("\n")) {
            sum = sum + priority(sameItems(string));
        }
        return String.valueOf(sum);
    }

    @Override
    public String part2(String input) {
        int sum = 0;
        String[] strings = input.split("\n");
        for (int i = 0; i < strings.length; i = i + 3) {
            sum = sum + priority(sameItems2(strings[i], strings[i + 1], strings[i + 2]));
        }
        return String.valueOf(sum);
    }

    public static String sameItems(String string) {
        String part1 = string.substring(0, string.length() / 2);
        String part2 = string.substring((string.length() / 2));
        for (int i = 0; i < part1.length(); i++) {
            if (part2.contains(part1.substring(i, i + 1))) {
                return (part1.substring(i, i + 1));
            }
        }
        return "";
    }

    public static String sameItems2(String one, String two, String three) {
        for (int i = 0; i < one.length(); i++) {
            if (two.contains(one.substring(i, i + 1)) & (three.contains(one.substring(i, i + 1)))) {
                return (one.substring(i, i + 1));
            }
        }
        return "";
    }

    public static int priority(String item) {
        int i = item.getBytes()[0];
        if (i > 96) {
            return i - 96;
        }
        return i - 64 + 26;
    }
}
