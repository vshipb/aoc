package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day19Test {

    Puzzle p = new Day19();

    String input = "Blueprint 1: Each ore robot costs 4 ore." +
            " Each clay robot costs 2 ore." +
            " Each obsidian robot costs 3 ore and 14 clay." +
            " Each geode robot costs 2 ore and 7 obsidian." +
            "\n" +
            "Blueprint 2:" +
            "  Each ore robot costs 2 ore." +
            "  Each clay robot costs 3 ore." +
            "  Each obsidian robot costs 3 ore and 8 clay." +
            "  Each geode robot costs 3 ore and 12 obsidian.";

    @Test
    public void part1(){
        Assertions.assertEquals("33", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("58", p.part2(input));
    }
}
