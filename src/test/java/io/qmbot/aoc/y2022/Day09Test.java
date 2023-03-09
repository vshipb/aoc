package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day09Test {
    Puzzle p = new Day09();

    String input = "R 4\n" +
            "U 4\n" +
            "L 3\n" +
            "D 1\n" +
            "R 4\n" +
            "D 1\n" +
            "L 5\n" +
            "R 2";
    String input2 = "R 5\n" +
            "U 8\n" +
            "L 8\n" +
            "D 3\n" +
            "R 17\n" +
            "D 10\n" +
            "L 25\n" +
            "U 20";

    @Test
    public void part1(){
        Assertions.assertEquals("13", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("1", p.part2(input));
        Assertions.assertEquals("36", p.part2(input2));
    }
}
