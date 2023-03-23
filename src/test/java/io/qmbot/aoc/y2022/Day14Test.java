package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day14Test {
    Puzzle p = new Day14();

    String input = "498,4 -> 498,6 -> 496,6\n" +
            "503,4 -> 502,4 -> 502,9 -> 494,9";

    @Test
    public void part1(){
        Assertions.assertEquals("24", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("93", p.part2(input));
    }
}
