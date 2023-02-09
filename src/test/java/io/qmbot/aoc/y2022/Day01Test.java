package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day01Test {

    Puzzle p = new Day01();

    String input = "1000\n" +
            "2000\n" +
            "3000\n" +
            "\n" +
            "4000\n" +
            "\n" +
            "5000\n" +
            "6000\n" +
            "\n" +
            "7000\n" +
            "8000\n" +
            "9000\n" +
            "\n" +
            "10000";

    @Test
    public void part1(){
        Assertions.assertEquals("24000", p.part1(input));
    }

    @Test
    void part2(){
        Assertions.assertEquals("45000", p.part2(input));
    }
}
