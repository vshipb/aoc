package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day04 implements Puzzle {
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\22_4.txt";

    public static void main(String[] args) throws IOException {
        Puzzle puzzle = new Day04();
        String input = Files.readString(Paths.get(PATH));
        System.out.println(puzzle.part1(input));
        System.out.println(puzzle.part2(input));
    }

    @Override
    public String part1(String input) {
        int sum = 0;
        for (String string : input.split("\n")) {
            String[] elves = string.split("[,-]");
            int x = Integer.parseInt(elves[0]);
            int x1 = Integer.parseInt(elves[1]);
            int y = Integer.parseInt(elves[2]);
            int y1 = Integer.parseInt(elves[3]);
            if (((x <= y) & (x1 >= y1))
                    ||
                    ((x >= y) & (x1 <= y1))) {
                sum = sum + 1;
            }
        }
        return String.valueOf(sum);
    }

    @Override
    public String part2(String input) {
        int sum = 0;
        for (String string : input.split("\n")) {
            String[] elves = string.split("[,-]");
            int x = Integer.parseInt(elves[0]);
            int x1 = Integer.parseInt(elves[1]);
            int y = Integer.parseInt(elves[2]);
            int y1 = Integer.parseInt(elves[3]);
            if (((x <= y) & (x1 >= y1))
                    ||
                    ((x >= y) & (x1 <= y1))
                    ||
                    ((x <= y) & (x1 >= y))
                    ||
                    ((x1 >= y1) & (x <= y1))) {
                sum = sum + 1;
            }
        }
        return String.valueOf(sum);
    }
}
