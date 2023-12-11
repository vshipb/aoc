package io.qmbot.aoc.y2023;

import io.qmbot.aoc.Puzzle;

public class Day03 implements Puzzle {
    @Override
    public Object part1(String input) {
        char[][] schematic = parse(input);
        int sizeY = schematic.length;
        int sizeX = schematic[0].length;
        Number now;
        int result = 0;
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (Character.isDigit(schematic[y][x])) {
                    now = (newNumber(y, x, schematic));
                    if (now.isAdjacent(schematic, y, x)) {
                        result += now.value;
                        x += now.size - 1;
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Object part2(String input) {
        char[][] schematic = parse(input);
        int sizeY = schematic.length;
        int sizeX = schematic[0].length;
        long result = 0;
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                if (schematic[y][x] == '*') {
                    if (isAdjacent(schematic, y, x)){
                        result += gearRatio(schematic, y, x);
                    }
                }
            }
        }
        return result;
    }

    char[][] parse(String input) {
        String[] split = input.split(REGEX_NEW_LINE);
        int sizeY = split.length;
        int sizeX = split[0].length();
        char[][] schematic = new char[sizeY][sizeX];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                schematic[y][x] = split[y].charAt(x);
            }
        }
        return schematic;
    }

    public Number newNumber(int startY, int startX, char[][] schematic) {
        StringBuilder value = new StringBuilder();
        while (startX != schematic[0].length && Character.isDigit(schematic[startY][startX])) {
            value.append(schematic[startY][startX]);
            startX++;
        }
        return new Number(Integer.parseInt(value.toString()), value.length());
    }

    private long gearRatio(char[][] schematic, int startY, int startX) {
        char c;
        int maxY = schematic.length;
        int maxX = schematic[0].length;
        long result = 1;

        for (int y = startY - 1; y <= startY + 1; y++) {
            for (int x = startX - 1; x <= startX + 1; x++) {
                if (y >= 0 && y < maxY && x >= 0 && x < maxX && !(y == startY && x == startX)) {
                    c = schematic[y][x];
                    if (Character.isDigit(c)) {
                        while (x >= 0 && Character.isDigit(schematic[y][x])) {
                            x--;
                        }
                        Number n = newNumber(y, x + 1, schematic);
                        result *= n.value;
                        x += n.size;
                    }
                }
            }
        }
        return result;
    }

    private boolean isAdjacent(char[][] schematic, int startY, int startX) {
        char c;
        int maxY = schematic.length;
        int maxX = schematic[0].length;
        int result = 0;
        for (int y = startY - 1; y <= startY + 1; y++) {
            for (int x = startX - 1; x <= startX + 1; x++) {
                if (y >= 0 && y < maxY && x >= 0 && x < maxX && !(y == startY && x == startX)) {
                    c = schematic[y][x];
                    if (Character.isDigit(c)) {
                        result++;
                        break;
                    }
                }
            }
        }

        return result == 2;
    }


    static class Number {
        int value;
        int size;

        public Number(int value, int size) {
            this.value = value;
            this.size = size;
        }

        private boolean isAdjacent(char[][] schematic, int startY, int startX) {
            char c;
            int maxY = schematic.length;
            int maxX = schematic[0].length;
            for (int i = startX; i < startX + size; i++) {
                for (int y = startY - 1; y <= startY + 1; y++) {
                    for (int x = i - 1; x <= i + 1; x++) {
                        if (y >= 0 && y < maxY && x >= 0 && x < maxX && !(y == startY && x == i)) {
                            c = schematic[y][x];
                            if (!Character.isDigit(c) && c != '.') {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }
    }
}
