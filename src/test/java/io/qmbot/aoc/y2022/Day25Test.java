package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day25Test {
    Puzzle p = new Day25();
    String input = "1=-0-2\n" +
            "12111\n" +
            "2=0=\n" +
            "21\n" +
            "2=01\n" +
            "111\n" +
            "20012\n" +
            "112\n" +
            "1=-1=\n" +
            "1-12\n" +
            "12\n" +
            "1=\n" +
            "122";

    @Test
    public void addTest() {
        Assertions.assertEquals("2", Day25.SNAFU.parse("1").add(Day25.SNAFU.parse("1")).toString());
        Assertions.assertEquals("1=", Day25.SNAFU.parse("1").add(Day25.SNAFU.parse("2")).toString());
        Assertions.assertEquals("1-", Day25.SNAFU.parse("1=").add(Day25.SNAFU.parse("1")).toString());

    }
    @Test
    public void part1() {
        Assertions.assertEquals("2=-1=0", p.part1(input));
    }

    @Test
    public void part2() {
        Assertions.assertEquals("", p.part2(input));
    }
}
