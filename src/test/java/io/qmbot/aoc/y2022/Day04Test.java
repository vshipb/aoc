package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day04Test {
    Puzzle p = new Day04();

    String input = """
            2-4,6-8
            2-3,4-5
            5-7,7-9
            2-8,3-7
            6-6,4-6
            2-6,4-8""";

    @Test
    public void part1(){
        Assertions.assertEquals("2", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("4", p.part2(input));
    }
}
