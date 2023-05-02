package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day21Test {

    Puzzle p = new Day21();

    String input = "root: pppw + sjmn\n" +
            "dbpl: 5\n" +
            "cczh: sllz + lgvd\n" +
            "zczc: 2\n" +
            "ptdq: humn - dvpt\n" +
            "dvpt: 3\n" +
            "lfqf: 4\n" +
            "humn: 5\n" +
            "ljgn: 2\n" +
            "sjmn: drzm * dbpl\n" +
            "sllz: 4\n" +
            "pppw: cczh / lfqf\n" +
            "lgvd: ljgn * ptdq\n" +
            "drzm: hmdt - zczc\n" +
            "hmdt: 32";

    @Test
    public void part1(){
        Assertions.assertEquals("152", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("301", p.part2(input));
    }
}
