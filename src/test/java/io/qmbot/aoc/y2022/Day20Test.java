package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day20Test {
    Puzzle p = new Day20();

    String input = "1\n" +
            "2\n" +
            "-3\n" +
            "3\n" +
            "-2\n" +
            "0\n" +
            "4";

    @Test
    public void part1() {
        Assertions.assertEquals("3", p.part1(input));
    }

    @Test
    public void part2() {
        Assertions.assertEquals("301", p.part2(input));
    }
}
