package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day02 implements Puzzle {
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\22_2.txt";

    public static void main(String[] args) throws IOException {
        Puzzle puzzle = new Day02();
        String lines = Files.readString(Paths.get(PATH));
        System.out.println(puzzle.part1(lines));
        System.out.println(puzzle.part2(lines));
    }

    @Override
    public String part1(String input) {
        int count = 0;
        for (String round : input.split("\r\n")) {
            if (round.contains("X")) {
                count++;
            }
            if (round.contains("Y")) {
                count = count + 2;
            }
            if (round.contains("Z")) {
                count = count + 3;
            }
            if (((round.contains("A X")) || (round.contains("B Y")) || (round.contains("C Z")))) {
                count = count + 3;
            }
            if (((round.contains("A Y")) || (round.contains("B Z")) || (round.contains("C X")))) {
                count = count + 6;
            }
        }
        return String.valueOf(count);
    }

    @Override
    public String part2(String input) {
        int count = 0;
        for (String round : input.split("\r\n")) {
            if (round.contains("Y")) {
                count = count + 3;
            }
            if (round.contains("Z")) {
                count = count + 6;
            }
            if (((round.contains("A X")) || (round.contains("B Z")) || (round.contains("C Y")))) {
                count = count + 3;
            }
            if (((round.contains("A Z")) || (round.contains("B Y")) || (round.contains("C X")))) {
                count = count + 2;
            }
            if (((round.contains("A Y")) || (round.contains("B X")) || (round.contains("C Z")))) {
                count = count + 1;
            }
        }
        return String.valueOf(count);
    }
}
