package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;

public class Day01 implements Puzzle {
    @Override
    public String part1(String input) {
        return Long.toString(Arrays.stream(input.split("\n\n"))
                .mapToLong(s -> s.lines().mapToLong(Long::parseLong).sum())
                .max().orElseThrow());
    }

    @Override
    public String part2(String input) {
        long[] sort = Arrays.stream(input.split("\n\n"))
                .mapToLong(s -> s.lines().mapToLong(Long::parseLong).sum())
                .sorted().toArray();
        int length = sort.length;
        return Long.toString(sort[length - 1] + sort[length - 2] + sort[length - 3]);
    }
}
