package io.qmbot.aoc.y2023;

import io.qmbot.aoc.Puzzle;

import java.util.Arrays;

public class Day01 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).mapToInt(s -> calibrationValue(s, false)).sum();
    }

    @Override
    public Integer part2(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).mapToInt(s -> calibrationValue(s, true)).sum();
    }

    private String first(String string, boolean withWords) {
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (Character.isDigit(c)) return Character.toString(c);
            if (withWords) {
                for (NumberEnum number : NumberEnum.values()) {
                    String substring = string.substring(i);
                    if (substring.startsWith(number.string)) {
                        return String.valueOf(number.getNumber());
                    }
                }
            }
        }
        return null;
    }

    private String second(String string, boolean withWords) {
        for (int i = string.length() - 1; i > -1; i--) {
            char c = string.charAt(i);
            if (Character.isDigit(c)) return Character.toString(c);
            if (withWords) {
                for (NumberEnum number : NumberEnum.values()) {
                    int start = i - number.string.length() + 1;
                    if (start >= 0 && string.substring(start, i + 1).startsWith(number.string)) {
                        return String.valueOf(number.getNumber());
                    }
                }
            }
        }
        return null;
    }
    private int calibrationValue(String string, boolean withWords) {
        return Integer.parseInt(first(string ,withWords) + second(string , withWords));
    }

    public enum NumberEnum {
        ONE("one", 1),
        TWO("two", 2),
        THREE("three", 3),
        FOUR("four", 4),
        FIVE("five", 5),
        SIX("six", 6),
        SEVEN("seven", 7),
        EIGHT("eight", 8),
        NINE("nine", 9);

        private final String string;
        private final int number;

        NumberEnum(String string, int number) {
            this.string = string;
            this.number = number;
        }

        public String getString() {
            return string;
        }

        public int getNumber() {
            return number;
        }

        private int isNumber(String s) {
            if (s.equals(string)) return number;
            return 0;
        }
    }
}
