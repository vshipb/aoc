package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.HashSet;
import java.util.Set;

public class Day06 implements Puzzle {
    @Override
    public String part1(String input) {
        
        for (int i = 3; i < input.length(); i++) {
            Set<String> set = new HashSet<>();
            set.add(input.substring(i - 3, i - 2));
            set.add(input.substring(i - 2, i - 1));
            set.add(input.substring(i - 1, i));
            set.add(input.substring(i, i + 1));
            if (set.size() == 4) {
                return String.valueOf(i + 1);
            }
        }
        return "";
    }

    @Override
    public String part2(String input) {
        for (int i = 13; i < input.length(); i++) {
            Set<String> uniq = new HashSet<>();
            for (int j = i; j > (i - 14); j--) {
                uniq.add(input.substring(j, j + 1));
            }
            if (uniq.size() == 14)
                return String.valueOf(i + 1);
        }
        return "";
    }
}
