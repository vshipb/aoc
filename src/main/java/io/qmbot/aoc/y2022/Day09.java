package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.stream.IntStream;

public class Day09 implements Puzzle {

    @Override
    public String part1(String input) {
        return Integer.toString(positions(new Field(2, input)));
    }

    @Override
    public String part2(String input) {
        return Integer.toString(positions(new Field(10, input)));
    }

    private static int positions(Field field) {
        return Math.toIntExact(IntStream.range(0, 1000).flatMap(i -> IntStream.range(0, 1000).filter(j -> field.grid[i][j])).count());
    }

    static class Field {
        boolean[][] grid = new boolean[1000][1000];
        static int[] x;
        static int[] y;
        int knots;

        Field(int knots, String input) {
            this.knots = knots;
            x = new int[knots];
            y = new int[knots];
            for (int i = 0; i < knots; i++) {
                x[i] = 500;
                y[i] = 500;
            }
            this.moving(input);
        }

        private Field moving(String input) {
            for (String string : input.split(REGEX_NEW_LINE)) {
                String[] command = string.split(" ");
                for (int i = 0; i < Integer.parseInt(command[1]); i++) {
                    this.move(command[0]);
                }
            }
            return this;
        }

        void move(String command) {
            switch (command) {
                case "R" -> x[0]++;
                case "L" -> x[0]--;
                case "U" -> y[0]--;
                case "D" -> y[0]++;
                default -> throw new IllegalStateException("Illegal input");
            }
            moveTail();
            grid[y[knots - 1]][x[knots - 1]] = true;
        }

        void moveTail() {
            for (int i = 1; i < knots; i++) {
                if (Math.abs(x[i - 1] - x[i]) <= 1 && Math.abs(y[i - 1] - y[i]) <= 1) {
                    continue;
                }
                x[i] = x[i] + Integer.compare(x[i - 1], x[i]);
                y[i] = y[i] + Integer.compare(y[i - 1], y[i]);
            }
        }
    }
}

