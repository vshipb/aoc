package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;

public class Day02 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).mapToInt(round -> switch (round) {
            case "A X" -> 1 + 3;
            case "A Y" -> 2 + 6;
            case "A Z" -> 3;
            case "B X" -> 1;
            case "B Y" -> 2 + 3;
            case "B Z" -> 3 + 6;
            case "C X" -> 1 + 6;
            case "C Y" -> 2;
            case "C Z" -> 3 + 3;
            default -> throw new IllegalArgumentException();
        }).sum();
    }

    @Override
    public Integer part2(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).mapToInt(round -> switch (round) {
            case "A X" -> 3;
            case "A Y" -> 1 + 3;
            case "A Z" -> 2 + 6;
            case "B X" -> 1;
            case "B Y" -> 2 + 3;
            case "B Z" -> 3 + 6;
            case "C X" -> 2;
            case "C Y" -> 3 + 3;
            case "C Z" -> 1 + 6;
            default -> throw new IllegalArgumentException();
        }).sum();
    }
}
