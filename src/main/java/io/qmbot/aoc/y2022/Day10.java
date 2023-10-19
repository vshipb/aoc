package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        return recognize(drawing(input));
    }

    static String drawing(String input) {
        List<String> commands = Arrays.stream(input.split(REGEX_NEW_LINE)).flatMap(line -> Arrays.stream(line.split(" "))).toList();
        StringBuilder sb = new StringBuilder();
        int spriteX = 1;
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 40; x++) {
                sb.append(Math.abs(spriteX - x) <= 1 ? "#" : ".");
                spriteX = addX(spriteX, commands.get(y * 40 + x));
            }
            sb.append(REGEX_NEW_LINE);
        }
        sb.setLength(sb.length() - 1);
        return sb.toString();
    }

    private static int addX(int x, String cmd) {
        return cmd.equals("noop") || cmd.equals("addx") ? x : x + Integer.parseInt(cmd);
    }

    private static String recognize(String string) {
        String[] lines = string.split(REGEX_NEW_LINE);
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < lines[0].length() - 1; x += 5) {
            int finalX = x;
            result.append(recognizeChar(Arrays.stream(lines)
                    .map(line -> line.substring(finalX, finalX + 4) + REGEX_NEW_LINE).collect(Collectors.joining())));
        }
        return result.toString();
    }

    private static char recognizeChar(String string) {
        return switch (string) {
            case """
                    ###.
                    #..#
                    #..#
                    ###.
                    #.#.
                    #..#
                    """ -> 'R';
            case """
                    ####
                    ...#
                    ..#.
                    .#..
                    #...
                    ####
                    """ -> 'Z';
            case """
                    ####
                    #...
                    ###.
                    #...
                    #...
                    ####
                    """ -> 'E';
            case """
                    #..#
                    #.#.
                    ##..
                    #.#.
                    #.#.
                    #..#
                    """ -> 'K';
            case """
                    ####
                    #...
                    ###.
                    #...
                    #...
                    #...
                    """ -> 'F';
            case """
                    #..#
                    #..#
                    ####
                    #..#
                    #..#
                    #..#
                    """ -> 'H';
            case """
                    .##.
                    #..#
                    #..#
                    ####
                    #..#
                    #..#
                    """ -> 'A';
            default -> throw new IllegalArgumentException("Unknown char:\n" + string);
        };
    }
}
