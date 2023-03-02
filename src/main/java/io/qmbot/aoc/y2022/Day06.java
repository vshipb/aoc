package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day06 implements Puzzle{
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\22_6.txt";

    public static void main(String[] args) throws IOException {
        Puzzle puzzle = new Day06();
        String input = Files.readString(Paths.get(PATH));
        System.out.println(puzzle.part1(input));
        System.out.println(puzzle.part2(input));
    }

    @Override
    public String part1(String input) {
        
        for (int i = 3; i<input.length(); i++){
            String m1 = input.substring(i-3, i-2);
            String m2 = input.substring(i-2, i-1);
            String m3 = input.substring(i-1, i);
            String m4 = input.substring(i, i+1);
            if (m1.equals(m2) || m1.equals(m3) || m1.equals(m4) || m2.equals(m3) || m2.equals(m4) || m3.equals(m4)){
            } else {
                return String.valueOf(i+1);
            }
        }
        return "";
    }

    @Override
    public String part2(String input) {
        for (int i = 13; i<input.length(); i++) {
            Set<String> uniq = new HashSet<>();
            for (int j = i; j > (i - 14); j--) {
                uniq.add(input.substring(j, j + 1));
            }
            if (uniq.size() == 14){
                return String.valueOf(i+1);
            }
        }
        return "";
    }
}
