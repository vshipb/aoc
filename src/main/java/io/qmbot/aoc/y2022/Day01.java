package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

public class Day01 implements Puzzle {
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\2022_1.1.txt";

    public static void main(String[] args) throws IOException {
        Puzzle puzzle = new Day01();
        String input = Files.readString(Paths.get(PATH));
        System.out.println(puzzle.part1(input));
        System.out.println(puzzle.part2(input));
    }
    @Override
    public String part1(String input) {
//        int elvCaloriesMax = 0;
//        int elv = 0;
//        for (String line : input.lines().toList()) {
//            if (line.isEmpty()) {
//                if (elv > elvCaloriesMax) {
//                    elvCaloriesMax = elv;
//                }
//                elv = 0;
//            } else {
//                elv = elv + Integer.parseInt(line);
//            }
//        }
//        System.out.println(elvCaloriesMax);

        return Long.toString(Arrays.stream(input.split("\r\n\r\n"))
                .mapToLong(s -> s.lines().mapToLong(Long::parseLong).sum())
                .max().orElseThrow());
    }


    @Override
    public String part2(String input) {

//        int elv1 = 0;
//        int elv2 = 0;
//        int elv3 = 0;
//        int elv = 0;
//        for (String line : input.lines().toList()) {
//            if (line.isEmpty()) {
//                if (elv > elv1) {
//                    elv3 = elv2;
//                    elv2 = elv1;
//                    elv1 = elv;
//                } else if (elv > elv2) {
//                    elv3 = elv2;
//                    elv2 = elv;
//                }
//                elv = 0;
//            } else {
//                elv = elv + Integer.parseInt(line);
//                if ((elv > elv3) & (elv < elv2)) {
//                    elv3 = elv;
//                }
//            }
//        }
//        int sum = elv2 + elv1 + elv3;
//        System.out.println(sum);

        long[] sort = Arrays.stream(input.split("\r\n\r\n"))
                .mapToLong(s -> s.lines().mapToLong(Long::parseLong).sum())
                .sorted().toArray();
        int length = sort.length;
        return Long.toString(sort[length-1]+sort[length-2]+sort[length-3]);
    }
}
