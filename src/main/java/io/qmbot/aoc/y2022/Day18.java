package io.qmbot.aoc.y2022;

import static java.lang.Integer.parseInt;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;


public class Day18 implements Puzzle {
    @Override
    public String part1(String input) {
        boolean[][][] lava = new boolean[20][20][20];

        List<Point> points = new ArrayList<>();

        Arrays.stream(input.split("\n")).map(s -> s.split(","))
                .forEach(crd -> lava[parseInt(crd[0])][parseInt(crd[1])][parseInt(crd[2])] = true);

        for (String string : input.split("\n")) {
            int[] crd = Stream.of(string.split(",")).mapToInt(Integer::parseInt).toArray();

            points.add(new Point(crd[0], crd[1], crd[2]));
        }

        return String.valueOf(result(lava, points));
    }

    @Override
    public String part2(String input) {
        int size = 30;
        boolean[][][] lava = new boolean[size][size][size];
        int[][][] water = new int[size][size][size];
        List<Point> points = new ArrayList<>();

        Arrays.stream(input.split("\n")).map(s -> s.split(","))
                .forEach(crd -> lava[parseInt(crd[0]) + 1][parseInt(crd[1]) + 1][parseInt(crd[2]) + 1] = true);

        fullWater(lava, water);

        for (int z = 0; z < size; z++) {
            for (int y = 0; y < size; y++) {
                for (int x = 0; x < size; x++) {
                    if (water[z][y][x] > 0) {
                        lava[z][y][x] = true;
                        points.add(new Point(z, y, x));
                    } else {
                        lava[z][y][x] = false;
                    }
                }
            }
        }

        return String.valueOf(result(lava, points) - 6 * size * size);
    }

    private int result(boolean[][][] lava, List<Point> points) {
        int result = 0;

        for (Point point : points) {
            result += checkNeighbors(lava, point.pointZ, point.pointY, point.pointX);
        }
        return result;
    }

    static class Point {
        int pointZ;
        int pointY;
        int pointX;

        public Point(int z, int y, int x) {
            this.pointZ = z;
            this.pointY = y;
            this.pointX = x;
        }

        List<Point> neighbors(int size) {
            List<Point> neighbors = new ArrayList<>();
            if (pointZ > 0) {
                neighbors.add(new Point(pointZ - 1, pointY, pointX));
            }
            if (pointZ < size - 1) {
                neighbors.add(new Point(pointZ + 1, pointY, pointX));
            }
            if (pointY > 0) {
                neighbors.add(new Point(pointZ, pointY - 1, pointX));
            }
            if (pointY < size - 1) {
                neighbors.add(new Point(pointZ, pointY + 1, pointX));
            }
            if (pointX > 0) {
                neighbors.add(new Point(pointZ, pointY, pointX - 1));
            }
            if (pointX < size - 1) {
                neighbors.add(new Point(pointZ, pointY, pointX + 1));
            }
            return neighbors;
        }
    }

    static void fullWater(boolean[][][] lava, int[][][] water) {
        boolean needNumber = true;
        int needInt = 1;
        water[0][0][0] = 1;
        int size = water.length;

        while (needNumber) {
            List<Point> nowPoints = new ArrayList<>();
            needNumber = false;
            for (int z = 0; z < size; z++) {
                for (int y = 0; y < size; y++) {
                    for (int x = 0; x < size; x++) {
                        if (water[z][y][x] == needInt) {
                            needNumber = true;
                            nowPoints.add(new Point(z, y, x));
                        }
                    }
                }
            }
            needInt++;

            for (Point point : nowPoints) {
                List<Point> neighbors = point.neighbors(size);
                for (Point neighbor : neighbors) {
                    int z = neighbor.pointZ;
                    int y = neighbor.pointY;
                    int x = neighbor.pointX;
                    if (water[z][y][x] == 0 && !lava[z][y][x])
                        water[z][y][x] = needInt;
                }
            }
        }
    }

    static int checkNeighbors(boolean[][][] field, int z, int y, int x) {
        int result = 6;
        int size = field.length;

        for (int i = -1; i < 2; i += 2) {
            if (z + i >= 0 && z + i < size && field[z + i][y][x]) {
                result--;
            }
            if (y + i >= 0 && y + i < size && field[z][y + i][x]) {
                result--;
            }
            if (x + i >= 0 && x + i < size && field[z][y][x + i]) {
                result--;
            }
        }

        return result;
    }
}
