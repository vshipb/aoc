package io.qmbot.aoc;

public interface Puzzle {
    String REGEX_NEW_LINE = "\n";

    String REGEX_EMPTY_LINE = "\n\n";

    String part1(String input);

    String part2(String input);
}