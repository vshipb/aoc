package io.qmbot.aoc.y2023;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day03Test {
    Puzzle p = new Day03();

    String input = """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...$.*....
            .664.598..""";

    @Test
    public void part1(){
        Assertions.assertEquals(4361, p.part1(input));
    }

    @Test
    void part2(){
        Assertions.assertEquals(467835L, p.part2(input));
    }
}
