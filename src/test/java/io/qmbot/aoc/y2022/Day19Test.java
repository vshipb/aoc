package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static io.qmbot.aoc.Puzzle.REGEX_NEW_LINE;
import static io.qmbot.aoc.y2022.Day19.star;

public class Day19Test {

    Puzzle p = new Day19();

    String input = """
            Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
            Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.""";

    @Test
    public void part1(){
        Assertions.assertEquals("33", p.part1(input));
    }

    @Test
    public void bestGeodesPart2() {
        List<Day19.Blueprint> blueprintList = new ArrayList<>();
        for (String string : input.split(REGEX_NEW_LINE)) {
            blueprintList.add(new Day19.Blueprint(string));
        }
        Assertions.assertEquals(56, star(blueprintList.get(0), 32));
        Assertions.assertEquals(62, star(blueprintList.get(1), 32));
    }
}
