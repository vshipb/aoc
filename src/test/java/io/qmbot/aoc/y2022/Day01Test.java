package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day01Test {

    Puzzle p = new Day01();

    String input = """
            1000
            2000
            3000

            4000

            5000
            6000

            7000
            8000
            9000

            10000""";

    @Test
    public void part1(){
        Assertions.assertEquals("24000", p.part1(input));
    }

    @Test
    void part2(){
        Assertions.assertEquals("45000", p.part2(input));
    }
}
