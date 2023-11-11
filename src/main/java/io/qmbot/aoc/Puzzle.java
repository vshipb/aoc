package io.qmbot.aoc;

public interface Puzzle {
    String REGEX_NEW_LINE = "\n";

    String REGEX_EMPTY_LINE = "\n\n";

    Object part1(String input);

    Object part2(String input);
}