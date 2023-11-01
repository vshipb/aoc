package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;

public class Day06 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return markerPosition(input, 4);
    }

    @Override
    public Integer part2(String input) {
        return markerPosition(input, 14);
    }

    private static int markerPosition(String input, int markerSize) {
        for (int i = markerSize - 1; i < input.length(); i++) {
            if (input.substring(i - markerSize + 1, i + 1).chars().distinct().count() == markerSize)
                return i + 1;
        }
        throw new IllegalStateException("Input without marker");
    }
}
