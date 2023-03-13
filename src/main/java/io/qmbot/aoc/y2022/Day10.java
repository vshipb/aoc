package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day10 implements Puzzle {
    @Override
    public String part1(String input) {
        List<String> commands = new ArrayList<>();
        for (String string : input.split("\n")) {
            String[] split = string.split(" ");
            commands.addAll(Arrays.asList(split));
        }
        int x = 1;
        int cycles = 0;
        int signals = 0;
        for (String string : commands) {
            cycles++;
            if ((cycles - 20) % 40 == 0) {
                signals = signals + cycles * x;
            }
            if (cycles == 220) {
                break;
            }
            if (!string.equals("noop") && !string.equals("addx")) {
                x = x + Integer.parseInt(string);
            }
        }
        return Integer.toString(signals);
    }

    @Override
    public String part2(String input) {
        List<String> commands = new ArrayList<>();
        for (String string : input.split("\n")) {
            String[] split = string.split(" ");
            commands.addAll(Arrays.asList(split));
        }
        StringBuilder string = new StringBuilder();
        int spriteX = 1;
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 40; x++) {
                int index = y * 40 + x;
                if (Math.abs(spriteX - x) <= 1) {
                    string.append("#");
                } else {
                    string.append(".");
                }
                String cmd = commands.get(index);
                if (!cmd.equals("noop") && !cmd.equals("addx")) {
                    spriteX = spriteX + Integer.parseInt(cmd);
                }
            }
            string.append("\n");
        }
        int endOfString = string.length();
        string.delete(endOfString - 1, endOfString);
        return string.toString();
    }
}
