package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;

public class Day14 implements Puzzle {
    @Override
    public String part1(String input) {
        List<String> strings = List.of(input.split("\n"));
        int maxX = 0;
        int maxY = 0;
        int minX = 1000;
        for (String string : strings) {
            String[] str = string.split(" -> ");
            for (String s : str) {
                String[] coordinates = s.split(",");
                int x = Integer.parseInt(coordinates[0]);
                int y = Integer.parseInt(coordinates[1]);
                if (maxX < x) maxX = x;
                if (maxY < y) maxY = y;
                if (minX > x) minX = x;
            }
        }
        int fieldSize = maxX - minX + 3;
        int delete = maxX - fieldSize + 2;
        int startFallX = 500 - delete;
        int[][] field = new int[maxY + 2][fieldSize];

        for (String string : strings) {
            rocks(string, field, delete);
        }
        int countOfSend = 0;

        while (field[0][startFallX] == 0) {
            fall(field, startFallX);
        }

        for (int[] ints : field) {
            for (int j = 0; j < field[1].length; j++) {
                if (ints[j] == 1) {
                    countOfSend++;
                }
            }
        }
        return Integer.toString(countOfSend - 1);
    }

    @Override
    public String part2(String input) {
        List<String> strings = List.of(input.split("\n"));
        int maxY = 0;
        for (String string : strings) {
            String[] str = string.split(" -> ");
            for (String s : str) {
                String[] coordinates = s.split(",");
                int y = Integer.parseInt(coordinates[1]);
                if (maxY < y) maxY = y;
            }
        }
        int delete = 0;
        int startFallX = 500 - delete;

        int[][] field = new int[maxY + 3][1000];

        for (String string : strings) {
            rocks(string, field, delete);
        }
        for (int i = 0; i < field[0].length; i++) {
            field[field.length - 1][i] = 2;
        }

        int countOfSend = 0;
        while (field[0][startFallX] == 0) {
            fall(field, startFallX);
        }

        for (int[] ints : field) {
            for (int j = 0; j < field[1].length; j++) {
                if (ints[j] == 1) {
                    countOfSend++;
                }
            }
        }

        return Integer.toString(countOfSend);
    }

    static class Rock {
        int rockX;
        int rockY;

        Rock(int y, int x) {
            this.rockX = x;
            this.rockY = y;
        }

    }

    private static void rocks(String string, int[][] field, int delete) {
        List<Rock> rocks = new ArrayList<>();
        String[] split = string.split(" -> ");
        for (String s : split) {
            String[] split1 = s.split(",");
            int x = Integer.parseInt(split1[0]) - delete;
            int y = Integer.parseInt(split1[1]);
            rocks.add(new Rock(y, x));
        }
        for (int i = 1; i < rocks.size(); i++) {
            int rockFromX = rocks.get(i - 1).rockX;
            int rockToX = rocks.get(i).rockX;
            int rockFromY = rocks.get(i - 1).rockY;
            int rockToY = rocks.get(i).rockY;
            if (rockFromY != rockToY) {
                int minY = Math.min(rockToY, rockFromY);
                int maxY = Math.max(rockToY, rockFromY);
                for (int j = minY; j <= maxY; j++) {
                    field[j][rockToX] = 2;
                }
            } else if (rockFromX != rockToX) {
                int minX = Math.min(rockToX, rockFromX);
                int maxX = Math.max(rockToX, rockFromX);
                for (int j = minX; j <= maxX; j++) {
                    field[rockFromY][j] = 2;
                }
            }
        }
    }


    private static void fall(int[][] field, int x) {
        int y = 0;
        int checkY = y + 1;
        int startX = x;
        if (field[checkY][x] == 1
                && field[checkY][x - 1] == 1
                && field[checkY][x + 1] == 1) {
            field[y][x] = 1;
        }
        while (field[y + 1][x] == 0
                || field[checkY][x - 1] == 0
                || field[checkY][x + 1] == 0) {
            field[y][x] = 0;
            if (field[checkY][x] == 0) {
                y++;
            } else if (field[checkY][x - 1] == 0) {
                y++;
                x--;
            } else if (field[checkY][x + 1] == 0) {
                y++;
                x++;
            }
            if (checkY == y) checkY++;

            field[y][x] = 1;

            if (y == field.length - 1) {
                field[0][startX] = 2;
                return;
            }
        }

    }

}
