package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;

public class Day08 implements Puzzle {
    @Override
    public String part1(String input) {
        int size = input.split("\n").length;
        int[][] forest = new int[size][size];
        int y = 0;
        for (String string : input.split("\n")) {
            for (int x = 0; x < size; x++) {
                forest[y][x] = Integer.parseInt(String.valueOf(string.charAt(x)));
            }
            y++;
        }
        int visible = 0;
        for (y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (isVisibleTop(forest, y, x)
                        || isVisibleBottom(forest, y, x, size)
                        || isVisibleLeft(forest, y, x)
                        || isVisibleRight(forest, y, x, size)) {
                    visible++;
                }
            }
        }
        return Integer.toString(visible);
    }

    @Override
    public String part2(String input) {
        int size = input.split("\n").length;
        int[][] forest = new int[size][size];
        int y = 0;
        for (String string : input.split("\n")) {
            for (int x = 0; x < size; x++) {
                forest[y][x] = Integer.parseInt(String.valueOf(string.charAt(x)));
            }
            y++;
        }
        int bestPlace = 0;
        for (y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                int top = treesVisibleTop(forest, y, x);
                int bottom = treesVisibleBottom(forest, y, x, size);
                int left = treesVisibleLeft(forest, y, x);
                int right = treesVisibleRight(forest, y, x, size);
                int visible = top * bottom * left * right;
                if (visible > bestPlace) {
                    bestPlace = visible;
                }
            }
        }
        return String.valueOf(bestPlace);
    }

    private static boolean isVisibleTop(int[][] forest, int y, int x) {
        for (int i = y - 1; i > -1; i--) {
            if (forest[y][x] <= forest[i][x]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isVisibleBottom(int[][] forest, int y, int x, int size) {
        for (int i = y + 1; i < size; i++) {
            if (forest[y][x] <= forest[i][x]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isVisibleLeft(int[][] forest, int y, int x) {
        for (int i = x - 1; i > -1; i--) {
            if (forest[y][x] <= forest[y][i]) {
                return false;
            }
        }
        return true;
    }

    private static boolean isVisibleRight(int[][] forest, int y, int x, int size) {
        for (int i = x + 1; i < size; i++) {
            if (forest[y][x] <= forest[y][i]) {
                return false;
            }
        }
        return true;
    }
    ////// 2 /////////////////////////////////////////////////////////////////////////////

    private static int treesVisibleTop(int[][] forest, int y, int x) {
        int trees = 0;
        for (int i = y - 1; i > -1; i--) {
            trees++;
            if (forest[y][x] <= forest[i][x]) {
                break;
            }
        }
        return trees;
    }

    private static int treesVisibleBottom(int[][] forest, int y, int x, int size) {
        int trees = 0;
        for (int i = y + 1; i < size; i++) {
            trees++;
            if (forest[y][x] <= forest[i][x]) {
                break;
            }
        }
        return trees;
    }

    private static int treesVisibleLeft(int[][] forest, int y, int x) {
        int trees = 0;
        for (int i = x - 1; i > -1; i--) {
            trees++;
            if (forest[y][x] <= forest[y][i]) {
                break;
            }
        }
        return trees;
    }

    private static int treesVisibleRight(int[][] forest, int y, int x, int size) {
        int trees = 0;
        for (int i = x + 1; i < size; i++) {
            trees++;
            if (forest[y][x] <= forest[y][i]) {
                break;
            }
        }
        return trees;
    }
}

