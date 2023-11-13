package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day06Test {

    Puzzle p = new Day06();
    String input = "mjqjpqmgbljsphdztnvjfqwrcgsmlb";
    String input2 = "bvwbjplbgvbhsrlpgdmjqwftvncz";
    String input3 = "nppdvjthqldpwncqszvftbrmjlhg";
    String input4 = "nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg";
    String input5 = "zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw";


    @Test
    public void test1() {
        Assertions.assertEquals(7, p.part1(input));
        Assertions.assertEquals(5, p.part1(input2));
        Assertions.assertEquals(6, p.part1(input3));
        Assertions.assertEquals(10, p.part1(input4));
        Assertions.assertEquals(11, p.part1(input5));
    }

    @Test
    public void test2() {
        Assertions.assertEquals(19, p.part2(input));
        Assertions.assertEquals(23, p.part2(input2));
        Assertions.assertEquals(23, p.part2(input3));
        Assertions.assertEquals(29, p.part2(input4));
        Assertions.assertEquals(26, p.part2(input5));
    }
}
