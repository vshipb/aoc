package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;

public class Day04 implements Puzzle {
    private static final String REGEX = "[,-]";

    @Override
    public Integer part1(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).mapToInt(line -> {
            int[] elves = Arrays.stream(line.split(REGEX)).mapToInt(Integer::parseInt).toArray();
            return fullyContains(elves[0], elves[1], elves[2], elves[3]) ? 1 : 0;
        }).sum();
    }

    @Override
    public Integer part2(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).mapToInt(line -> {
            int[] elves = Arrays.stream(line.split(REGEX)).mapToInt(Integer::parseInt).toArray();
            return overlapAtAll(elves[0], elves[1], elves[2], elves[3]) ? 1 : 0;
        }).sum();
    }

    static boolean fullyContains(int firstStart, int firstEnd, int secondStart, int secondEnd) {
        return ((firstStart <= secondStart) && (firstEnd >= secondEnd)) || ((firstStart >= secondStart) && (firstEnd <= secondEnd));
    }

    static boolean overlapAtAll(int firstStart, int firstEnd, int secondStart, int secondEnd) {
        return (firstEnd >= secondStart && firstStart <= secondStart) || (secondEnd >= firstStart && secondStart <= firstStart);
    }
}
