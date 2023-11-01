package io.qmbot.aoc.y2020;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day19Test {
    Puzzle p = new Day19();

    String input = """
            0: 4 1 5
            1: 4 4 | 5 5
            2: 4 4 | 5 5
            3: 4 5 | 5 4
            4: "a"
            5: "b""";
    String input2 = """
            0: 4 1 5
            1: 2 3 | 3 2
            2: 4 4 | 5 5
            3: 4 5 | 5 4
            4: "a"
            5: "b"

            ababbb
            bababa
            abbbab
            aaabbb
            aaaabbb""";

    @Test
    public void part1(){
        Assertions.assertEquals("2", p.part1(input2));
    }

    @Test
    void part2(){
        Assertions.assertEquals("45000", p.part2(input2));
    }
}
