package io.qmbot.aoc.y2022;


import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day02Test {
    Puzzle p = new Day02();

    String input = """
            A Y
            B X
            C Z""";

    @Test
    public void part1() {
        Assertions.assertEquals("15", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("12", p.part2(input));
    }
}