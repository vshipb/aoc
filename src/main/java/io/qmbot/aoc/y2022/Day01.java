package io.qmbot.aoc.y2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day01 {
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\2022_1.txt";

    public static void main(String[] args) throws IOException {
        List<String> allCalories = Files.lines(Paths.get(PATH)).collect(Collectors.toList());
        System.out.println(part1(allCalories));
        System.out.println(part2(allCalories));
    }

    private static String part1(List<String> allCalories) {
        int elv1 = 0;
        int elv = 0;
        for (String line : allCalories) {
            if (line.isEmpty()) {
                if (elv > elv1) {
                    elv1 = elv;
                }
                elv = 0;
            } else {
                elv = elv + Integer.parseInt(line);
            }
        }
        return Integer.toString(elv1);
    }

    private static String part2(List<String> allCalories) {
        int elv1 = 0;
        int elv2 = 0;
        int elv3 = 0;
        int elv = 0;
        for (String line : allCalories) {
            if (line.isEmpty()) {
                if (elv > elv1) {
                    elv3 = elv2;
                    elv2 = elv1;
                    elv1 = elv;
                } else if (elv > elv2) {
                    elv3 = elv2;
                    elv2 = elv;
                }
                elv = 0;
            } else {
                elv = elv + Integer.parseInt(line);
                if ((elv > elv3) & (elv < elv2)) {
                    elv3 = elv;
                }
            }
        }
        int sum = elv2 + elv1 + elv3;
        return Integer.toString(sum);
    }
}
