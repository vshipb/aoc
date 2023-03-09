package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;

public class Day09 implements Puzzle {

    @Override
    public String part1(String input) {
        Field field = new Field(2);
        for (String string : input.split("\n")) {
            String[] command = string.split(" ");
            String move = command[0];
            int steps = Integer.parseInt(command[1]);
            for (int i = 0; i < steps; i++) {
                field.move(move);
            }
        }
        int an = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (field.grid[i][j]) {
                    an++;
                }
            }
        }
        return Integer.toString(an);
    }

    @Override
    public String part2(String input) {
        Field field = new Field(10);
        for (String string : input.split("\n")) {
            String[] command = string.split(" ");
            String move = command[0];
            int steps = Integer.parseInt(command[1]);
            for (int i = 0; i < steps; i++) {
                field.move(move);
            }
        }
        int an = 0;
        for (int i = 0; i < 1000; i++) {
            for (int j = 0; j < 1000; j++) {
                if (field.grid[i][j]) {
                    an++;
                }
            }
        }
        return Integer.toString(an);
    }

    static class Field {
        boolean[][] grid = new boolean[1000][1000];
        static int[] x;
        static int[] y;
        int knots;

        Field(int knots) {
            this.knots = knots;
            x = new int[knots];
            y = new int[knots];
            for (int i = 0; i < knots; i++) {
                x[i] = 500;
                y[i] = 500;
            }
        }

        void move(String command) {
            switch (command) {
                case "R" -> x[0]++;
                case "L" -> x[0]--;
                case "U" -> y[0]--;
                case "D" -> y[0]++;
                default -> System.out.println("недопустимое направление");
            }
            moveTail();
            grid[y[knots - 1]][x[knots - 1]] = true;
        }

        void moveTail() {
            for (int i = 1; i < knots; i++) {
                if (Math.abs(x[i - 1] - x[i]) <= 1 && Math.abs(y[i - 1] - y[i]) <= 1) {
                    continue;
                }
                int dx = Integer.compare(x[i - 1], x[i]);
                int dy = Integer.compare(y[i - 1], y[i]);
                x[i] = x[i] + dx;
                y[i] = y[i] + dy;
            }
        }
    }
}

