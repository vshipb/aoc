package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Day07Test {
    Puzzle p = new Day07();

    String input = "$ cd /\n" +
            "$ ls\n" +
            "dir a\n" +
            "14848514 b.txt\n" +
            "8504156 c.dat\n" +
            "dir d\n" +
            "$ cd a\n" +
            "$ ls\n" +
            "dir e\n" +
            "29116 f\n" +
            "2557 g\n" +
            "62596 h.lst\n" +
            "$ cd e\n" +
            "$ ls\n" +
            "584 i\n" +
            "$ cd ..\n" +
            "$ cd ..\n" +
            "$ cd d\n" +
            "$ ls\n" +
            "4060174 j\n" +
            "8033020 d.log\n" +
            "5626152 d.ext\n" +
            "7214296 k";

    @Test
    public void test1() {
        Assertions.assertEquals("95437", p.part1(input));
    }

    @Test
    public void test2() {
        Assertions.assertEquals("24933642", p.part2(input));
    }
}
