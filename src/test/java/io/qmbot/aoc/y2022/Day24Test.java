package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day24Test {
    Puzzle p = new Day24();
    String input = """
            #.######
            #>>.<^<#
            #.<..<<#
            #>v.><>#
            #<^v^^>#
            ######.#""";

    @Test
    public void part1() {
        Assertions.assertEquals(18, p.part1(input));
    }

    @Test
    public void part2() {
        Assertions.assertEquals(54, p.part2(input));
    }
}
