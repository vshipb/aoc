package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Test;
import static io.qmbot.aoc.y2022.Day20.newIndex;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day20Test {
    Puzzle p = new Day20();

    String input = """
            1
            2
            -3
            3
            -2
            0
            4""";

    @Test
    public void testNewIndex() {
        assertEquals(newIndex(4, 5, 7), 3);
        assertEquals(newIndex(1, 0, 7), 1);
        assertEquals(newIndex(2, 0, 7), 2);
        assertEquals(newIndex(-3, 1, 7), 4);
        assertEquals(newIndex(3, 2, 7), 5);
        assertEquals(newIndex(-2, 2, 7), 6);
        assertEquals(newIndex(0, 3, 7), 3);
        assertEquals(newIndex(-2, 1, 7), 5);
        assertEquals(newIndex(1, 3, 7), 4);
        assertEquals(newIndex(0, 1, 2), 1);
        assertEquals(newIndex(0, 0, 2), 0);
        assertEquals(newIndex(0, 0, 1), 0);
        assertEquals(newIndex(-1, 0, 1), 0);
        assertEquals(newIndex(-50, 0, 1), 0);
        assertEquals(newIndex(50, 0, 1), 0);
        assertEquals(newIndex(1, 0, 1), 0);
    }

    @Test
    public void part1() {
        assertEquals("3", p.part1(input));
    }

    @Test
    public void part2() {
        assertEquals("1623178306", p.part2(input));
    }
}
