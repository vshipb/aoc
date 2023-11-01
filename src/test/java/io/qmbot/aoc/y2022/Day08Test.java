package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day08Test {
    Puzzle p = new Day08();

    String input = """
            30373
            25512
            65332
            33549
            35390""";

    @Test
    public void part1(){
        Assertions.assertEquals(21, p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals(8, p.part2(input));
    }
}
