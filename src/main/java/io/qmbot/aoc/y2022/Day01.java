package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;

public class Day01 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE + REGEX_NEW_LINE))
                .mapToInt(s -> s.lines().mapToInt(Integer::parseInt).sum())
                .max().orElseThrow();
    }

    @Override
    public Integer part2(String input) {
        int[] sort = Arrays.stream(input.split(REGEX_NEW_LINE + REGEX_NEW_LINE))
                .mapToInt(s -> s.lines().mapToInt(Integer::parseInt).sum())
                .sorted().toArray();
        int length = sort.length;
        return sort[length - 1] + sort[length - 2] + sort[length - 3];
    }
}
