package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day12Test {
    Puzzle p = new Day12();

    String input = """
            Sabqponm
            abcryxxl
            accszExk
            acctuvwj
            abdefghi""";

    @Test
    public void part1(){
        Assertions.assertEquals(31, p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals(29, p.part2(input));
    }
}
