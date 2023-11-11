package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day17Test {
    Puzzle p = new Day17();

    String input = ">>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>";

    @Test
    public void part1(){
        Assertions.assertEquals(3068, p.part1(input));
    }

    @Test
    public void part2(){Assertions.assertEquals(1514285714288L, p.part2(input));
    }
}
