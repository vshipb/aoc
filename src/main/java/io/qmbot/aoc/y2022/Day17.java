package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 implements Puzzle {
    @Override
    public String part1(String input) {
        String[][] field = new String[24276][9];

        fullField(field);

        int startDirection = 0;
        int start = field.length - 5;
        int indexF = 0;

        for (int i = 0; i < 2022; i++) {
            for (String[] string : field) {
                if (Arrays.asList(string).contains("7")) {
                    start = Arrays.asList(field).indexOf(string) - 4;
                    break;
                }
            }

            startDirection = createFigure(indexF, start).startFalling(field, input, startDirection);

            if (indexF == 4) indexF = -1;
            indexF++;
        }
        int high = 0;
        for (String[] string : field) {
            if (Arrays.asList(string).contains("7")) {
                high = Arrays.asList(field).indexOf(string);
                break;
            }
        }

        high = field.length - high - 1;
        return String.valueOf(high);
    }

    @Override
    public String part2(String input) {
        int highField = 150;
        String[][] field = new String[highField][9];
        long g = 0;

        fullField(field);

        int startDirection = 0;
        int start = field.length - 5;
        int indexF = 0;

        for (long numberF = 0; numberF < 1_000_000_000L; numberF++) {

            for (int i = 0; i < field.length; i++) {
                if (Arrays.asList(field[i]).contains("7")) {

                    if (i == 30 || i == 31 || i == 32) {
                        g = g + deleteBlocks(i, field, highField);
                        break;
                    }

                    start = Arrays.asList(field).indexOf(field[i]) - 4;
                    break;
                }
            }

            startDirection = createFigure(indexF, start).startFalling(field, input, startDirection);

            if (indexF == 4) indexF = -1;
            indexF++;
        }

        int high = 0;
        for (String[] string : field) {
            if (Arrays.asList(string).contains("7")) {
                high = Arrays.asList(field).indexOf(string);
                break;
            }
        }

        g = g + (field.length - high - 1);
        return String.valueOf(g);

    }

    private Figure createFigure(int indexF, int start) {
        Figure figure;

        switch (indexF) {
            case 0 -> figure = horizontal(start);
            case 1 -> figure = plus(start);
            case 2 -> figure = angle(start);
            case 3 -> figure = vertical(start);
            case 4 -> figure = square(start);
            default -> throw new IllegalArgumentException("Figure index " + indexF + " is not valid");
        }

        return figure;
    }

    private void fullField(String[][] field) {
        for (String[] strings : field) {
            Arrays.fill(strings, "0");
        }

        for (int j = 0; j < field.length; j++) {
            field[j][0] = "2";
            field[j][8] = "2";
        }
        for (int j = 0; j < field[0].length; j++) {
            field[field.length - 1][j] = "2";
        }
    }

    static long deleteBlocks(int i, String[][] field, int highField) {
        int max = highField - 2;
        int min = max - i;
        int change = i;
        for (int y = min; y < max; y++) {
            System.arraycopy(field[change], 1, field[y], 1, 7);
            change++;
        }
        for (int y = 0; y < min; y++) {
            for (int x = 1; x < 8; x++) {
                field[y][x] = "0";
            }
        }
        return min - i;
    }

    static class Point {
        int pointX;
        int pointY;

        public Point(int y, int x) {
            this.pointY = y;
            this.pointX = x;
        }
    }

    static class Figure {

        List<Point> figurePoints = new ArrayList<>();

        public Figure(Point... points) {
            figurePoints.addAll(Arrays.asList(points));
        }


        int startFalling(String[][] field, String direction, int startDirection) {
            boolean spaceForFalling = true;
            while (spaceForFalling) {
                boolean space = true;

                for (Point point : figurePoints) {
                    field[point.pointY][point.pointX] = "0";
                }
                int maxX = 1;
                int minX = 7;
                int maxY = 0;

                for (Point point : figurePoints) {
                    minX = Math.min(minX, point.pointX);
                    maxX = Math.max(maxX, point.pointX);
                    maxY = Math.max(maxY, point.pointY);
                }

                switch (direction.charAt(startDirection)) {
                    case '<' -> {
                        for (Point point : figurePoints) {
                            if (!field[point.pointY][point.pointX - 1].equals("0")) {
                                space = false;
                                break;
                            }
                        }
                        if (space) {
                            for (Point point : figurePoints) {
                                point.pointX = point.pointX - 1;
                            }
                        }
                    }
                    case '>' -> {
                        for (Point point : figurePoints) {
                            if (!field[point.pointY][point.pointX + 1].equals("0")) {
                                space = false;
                                break;
                            }
                        }
                        if (space) {
                            for (Point point : figurePoints) {
                                point.pointX = point.pointX + 1;
                            }
                        }
                    }
                    default -> throw new IllegalArgumentException("Figure index "
                            + direction.charAt(startDirection) + " is not valid");
                }
                startDirection++;
                if (startDirection == direction.length()) startDirection = 0;

                for (Point point : figurePoints) {
                    if (!field[point.pointY + 1][point.pointX].equals("0")) {
                        spaceForFalling = false;
                        break;
                    }
                }

                if (spaceForFalling) {
                    for (Point point : figurePoints) {
                        point.pointY = point.pointY + 1;
                    }
                }
                for (Point point : figurePoints) {
                    field[point.pointY][point.pointX] = "7";
                }
            }
            return startDirection;
        }
    }

    static Figure horizontal(int startY) {
        return new Figure(new Point(startY, 3), new Point(startY, 4), new Point(startY, 5),
                new Point(startY, 6));
    }

    static Figure plus(int startY) {
        return new Figure(new Point(startY, 4), new Point(startY - 1, 4), new Point(startY - 2, 4),
                new Point(startY - 1, 3), new Point(startY - 1, 5));
    }

    static Figure angle(int startY) {
        return new Figure(new Point(startY, 5), new Point(startY - 1, 5), new Point(startY - 2, 5),
                new Point(startY, 4), new Point(startY, 3));
    }

    static Figure vertical(int startY) {
        return new Figure(new Point(startY, 3), new Point(startY - 1, 3), new Point(startY - 2, 3),
                new Point(startY - 3, 3));
    }

    static Figure square(int startY) {
        return new Figure(new Point(startY, 3), new Point(startY, 4), new Point(startY - 1, 3),
                new Point(startY - 1, 4));
    }
}
