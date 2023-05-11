package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day23Test {

    Puzzle p = new Day23();

    String input = "..............\n" +
            "..............\n" +
            ".......#......\n" +
            ".....###.#....\n" +
            "...#...#.#....\n" +
            "....#...##....\n" +
            "...#.###......\n" +
            "...##.#.##....\n" +
            "....#..#......\n" +
            "..............\n" +
            "..............\n" +
            "..............";

    @Test
    public void part1(){
        Assertions.assertEquals("110", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("20", p.part2(input));
    }
}