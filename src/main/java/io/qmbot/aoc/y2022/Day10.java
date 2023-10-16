package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;
import java.util.List;

public class Day10 implements Puzzle {
    @Override
    public String part1(String input) {
        List<String> commands = Arrays.stream(input.split(REGEX_NEW_LINE)).flatMap(line -> Arrays.stream(line.split(" "))).toList();
        int x = 1;
        int cycles = 0;
        int signals = 0;
        for (String cmd : commands) {
            cycles++;
            if ((cycles - 20) % 40 == 0) {
                signals += cycles * x;
            }
            if (cycles == 220) {
                break;
            }
            x = addX(x, cmd);
        }
        return Integer.toString(signals);
    }

    @Override
    public String part2(String input) {
        List<String> commands = Arrays.stream(input.split(REGEX_NEW_LINE)).flatMap(line -> Arrays.stream(line.split(" "))).toList();
        StringBuilder string = new StringBuilder();
        int spriteX = 1;
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 40; x++) {
                string.append(Math.abs(spriteX - x) <= 1 ? "#" : ".");
                spriteX = addX(spriteX, commands.get(y * 40 + x));
            }
            string.append(REGEX_NEW_LINE);
        }
        string.setLength(string.length() - 1);
        return string.toString();
    }

    private static int addX(int x, String cmd) {
        return cmd.equals("noop") || cmd.equals("addx") ? x : x + Integer.parseInt(cmd);
    }
}
