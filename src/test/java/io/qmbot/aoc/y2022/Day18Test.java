package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day18Test {
    Puzzle p = new Day18();

    String input = """
            2,2,2
            1,2,2
            3,2,2
            2,1,2
            2,3,2
            2,2,1
            2,2,3
            2,2,4
            2,2,6
            1,2,5
            3,2,5
            2,1,5
            2,3,5""";

    @Test
    public void part1(){
        Assertions.assertEquals(64, p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals(58, p.part2(input));
    }
}
