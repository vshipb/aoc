package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class Day14 implements Puzzle {
    static final String PATH = " -> ";
    static final String COMMA = ",";

    @Override
    public Integer part1(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
        Parameters parameters = new Parameters(strings);
        int[][] field = new int[parameters.maxY + 3][parameters.fieldSize];
        strings.forEach(str -> rocks(str, field, parameters.delete));
        return resultAfterFalling(field, 500 - parameters.delete) - 1;
    }

    @Override
    public Integer part2(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
        Parameters parameters = new Parameters(strings);
        parameters.delete = 0;
        int[][] field = new int[parameters.maxY + 3][1000];
        strings.forEach(str -> rocks(str, field, parameters.delete));
        Arrays.fill(field[field.length - 1], 2);
        return resultAfterFalling(field, 500 - parameters.delete);
    }

    static class Parameters {
        int fieldSize;
        int delete;
        int maxY = 0;

        public Parameters(List<String> strings) {
            int maxX = 0;
            int minX = 1000;
            for (String string : strings) {
                for (String s : string.split(PATH)) {
                    String[] coordinates = s.split(COMMA);
                    int x = Integer.parseInt(coordinates[0]);
                    int y = Integer.parseInt(coordinates[1]);
                    if (maxY < y) this.maxY = y;
                    if (maxX < x) maxX = x;
                    if (minX > x) minX = x;
                }
            }
            this.fieldSize = maxX - minX + 3;
            this.delete = maxX - fieldSize + 2;
        }
    }

    static int resultAfterFalling(int[][] field, int startFallX) {
        while (field[0][startFallX] == 0) {
            fall(field, startFallX);
        }
        return Math.toIntExact(Arrays.stream(field).flatMapToInt(Arrays::stream).filter(value -> value == 1).count());
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
        List<Rock> rocks = new ArrayList<>(Arrays.stream(string.split(PATH)).map(s -> s.split(COMMA))
                .map(splitNumber -> new Rock(Integer.parseInt(splitNumber[1]), Integer.parseInt(splitNumber[0]) - delete)).toList());
        for (int i = 1; i < rocks.size(); i++) {
            int rockFromX = rocks.get(i - 1).rockX;
            int rockToX = rocks.get(i).rockX;
            int rockFromY = rocks.get(i - 1).rockY;
            int rockToY = rocks.get(i).rockY;
            if (rockFromY != rockToY) {
                IntStream.rangeClosed(Math.min(rockToY, rockFromY), Math.max(rockToY, rockFromY)).forEach(j -> field[j][rockToX] = 2);
            } else if (rockFromX != rockToX) {
                IntStream.rangeClosed(Math.min(rockToX, rockFromX), Math.max(rockToX, rockFromX)).forEach(j -> field[rockFromY][j] = 2);
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
        while (field[y + 1][x] == 0 || field[checkY][x - 1] == 0 || field[checkY][x + 1] == 0) {
            field[y][x] = 0;
            if (field[checkY][x] == 0) y++;
            else if (field[checkY][x - 1] == 0) {
                y++;
                x--;
            } else if (field[checkY][x + 1] == 0) {
                y++;
                x++;
            }
            checkY += (checkY == y) ? 1 : 0;
            field[y][x] = 1;
            if (y == field.length - 1) {
                field[0][startX] = 2;
                return;
            }
        }
    }
}
