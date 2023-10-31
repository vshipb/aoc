package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;

public class Day06 implements Puzzle {
    @Override
    public String part1(String input) {
        return markerPosition(input, 4);
    }

    @Override
    public String part2(String input) {
        return markerPosition(input, 14);
    }

    private static String markerPosition(String input, int markerSize) {
        for (int i = markerSize - 1; i < input.length(); i++) {
            if (input.substring(i - markerSize + 1, i + 1).chars().distinct().count() == markerSize)
                return String.valueOf(i + 1);
        }
        throw new IllegalStateException("Input without marker");
    }
}
