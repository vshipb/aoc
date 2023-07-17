package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day20Test {
    Puzzle p = new Day20();

    String input = "1\n" +
            "2\n" +
            "-3\n" +
            "3\n" +
            "-2\n" +
            "0\n" +
            "4";
    String input2 = "1";
    @Test
    public void newIndex() {
        // 1
        //2
        //-3
        //3
        //-2
        //0
        //4
        Assertions.assertEquals(Day20.newIndex(4, 5, 7), 3);

        Assertions.assertEquals(Day20.newIndex(1, 0, 7), 1);
        Assertions.assertEquals(Day20.newIndex(2, 0, 7), 2);
        Assertions.assertEquals(Day20.newIndex(-3, 1, 7), 4);
        Assertions.assertEquals(Day20.newIndex(3, 2, 7), 5);
        Assertions.assertEquals(Day20.newIndex(-2, 2, 7), 6);
        Assertions.assertEquals(Day20.newIndex(0, 3, 7), 3);

        Assertions.assertEquals(Day20.newIndex(-2, 1, 7), 5);
        Assertions.assertEquals(Day20.newIndex(1, 3, 7), 4);
        Assertions.assertEquals(Day20.newIndex(0, 1, 2), 1);
        Assertions.assertEquals(Day20.newIndex(0, 0, 2), 0);
        Assertions.assertEquals(Day20.newIndex(0, 0, 1), 0);
        Assertions.assertEquals(Day20.newIndex(-1, 0, 1), 0);
        Assertions.assertEquals(Day20.newIndex(-50, 0, 1), 0);
        Assertions.assertEquals(Day20.newIndex(50, 0, 1), 0);
        Assertions.assertEquals(Day20.newIndex(1, 0, 1), 0);
    }

    @Test
    public void part1() {
        Assertions.assertEquals("3", p.part1(input));
    }

    @Test
    public void part2() {
        Assertions.assertEquals("1623178306", p.part2(input));
    }
}
