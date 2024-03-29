package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day05Test {
    Puzzle p = new Day05();

    String input = """
                [D]
            [N] [C]
            [Z] [M] [P]
             1   2   3

            move 1 from 2 to 1
            move 3 from 1 to 3
            move 2 from 2 to 1
            move 1 from 1 to 2""";

    @Test
    public void test1() {
        Assertions.assertEquals("CMZ", p.part1(input));
    }

    @Test
    public void test2() {
        Assertions.assertEquals("MCD", p.part2(input));
    }
}
