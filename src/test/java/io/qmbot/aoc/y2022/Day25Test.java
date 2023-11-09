package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day25Test {
    Puzzle p = new Day25();
    String input = """
            1=-0-2
            12111
            2=0=
            21
            2=01
            111
            20012
            112
            1=-1=
            1-12
            12
            1=
            122""";

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
}
