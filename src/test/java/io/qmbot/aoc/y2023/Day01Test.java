package io.qmbot.aoc.y2023;

import io.qmbot.aoc.Puzzle;
import io.qmbot.aoc.y2023.Day01;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day01Test {
    Puzzle p = new Day01();

    String input = """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet""";

    String input2 = """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen""";

    @Test
    public void part1(){
        Assertions.assertEquals(142, p.part1(input));
    }

    @Test
    void part2(){
        Assertions.assertEquals(281, p.part2(input2));
    }
}
