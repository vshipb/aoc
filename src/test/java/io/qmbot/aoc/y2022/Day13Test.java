package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day13Test {
    Puzzle p = new Day13();

    String input = "[1,1,3,1,1]\n" +
            "[1,1,5,1,1]\n" +
            "\n" +
            "[[1],[2,3,4]]\n" +
            "[[1],4]\n" +
            "\n" +
            "[9]\n" +
            "[[8,7,6]]\n" +
            "\n" +
            "[[4,4],4,4]\n" +
            "[[4,4],4,4,4]\n" +
            "\n" +
            "[7,7,7,7]\n" +
            "[7,7,7]\n" +
            "\n" +
            "[]\n" +
            "[3]\n" +
            "\n" +
            "[[[]]]\n" +
            "[[]]\n" +
            "\n" +
            "[1,[2,[3,[4,[5,6,7]]]],8,9]\n" +
            "[1,[2,[3,[4,[5,6,0]]]],8,9]";

    @Test
    public void part1(){
        Assertions.assertEquals("13", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("140", p.part2(input));
    }
}
