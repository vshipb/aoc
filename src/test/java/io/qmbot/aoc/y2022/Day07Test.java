package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day07Test {
    Puzzle p = new Day07();

    String input = """
            $ сd /
            $ ls
            dir a
            14848514 b.txt
            8504156 c.dat
            dir d
            $ cd a
            $ ls
            dir e
            29116 f
            2557 g
            62596 h.lst
            $ cd e
            $ ls
            584 i
            $ cd ..
            $ cd ..
            $ cd d
            $ ls
            4060174 j
            8033020 d.log
            5626152 d.ext
            7214296 k""";

    @Test
    public void test1() {
        Assertions.assertEquals(95437, p.part1(input));
    }

    @Test
    public void test2() {
        Assertions.assertEquals(24933642, p.part2(input));
    }
}
