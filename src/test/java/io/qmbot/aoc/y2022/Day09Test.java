package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day09Test {
    Puzzle p = new Day09();

    String input = """
            R 4
            U 4
            L 3
            D 1
            R 4
            D 1
            L 5
            R 2""";
    String input2 = """
            R 5
            U 8
            L 8
            D 3
            R 17
            D 10
            L 25
            U 20""";

    @Test
    public void part1(){
        Assertions.assertEquals(13, p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals(1, p.part2(input));
        Assertions.assertEquals(36, p.part2(input2));
    }
}
