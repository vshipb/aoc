package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.stream.IntStream;

public class Day09 implements Puzzle {
    private static final int SIZE = 1000;

    @Override
    public Integer part1(String input) {
        return new Field(2).move(input).positions();
    }

    @Override
    public Integer part2(String input) {
        return new Field(10).move(input).positions();
    }

    static class Field {
        private final boolean[][] grid = new boolean[SIZE][SIZE];
        private static int[] x;
        private static int[] y;
        private final int knots;

        Field(int knots) {
            this.knots = knots;
            x = new int[knots];
            y = new int[knots];
            for (int i = 0; i < knots; i++) {
                x[i] = SIZE / 2;
                y[i] = SIZE / 2;
            }
        }

        private int positions() {
            return Math.toIntExact(IntStream.range(0, SIZE)
                            .flatMap(i -> IntStream.range(0, SIZE).filter(j -> grid[i][j])).count());
        }

        private Field move(String input) {
            for (String string : input.split(REGEX_NEW_LINE)) {
                String[] command = string.split(" ");
                for (int i = 0; i < Integer.parseInt(command[1]); i++) {
                    this.step(command[0]);
                }
            }
            return this;
        }

        private void step(String command) {
            switch (command) {
                case "R" -> x[0]++;
                case "L" -> x[0]--;
                case "U" -> y[0]--;
                case "D" -> y[0]++;
                default -> throw new IllegalArgumentException();
            }
            moveTail();
            grid[y[knots - 1]][x[knots - 1]] = true;
        }

        private void moveTail() {
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

