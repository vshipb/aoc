package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

public class Day12 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return new Field(List.of(input.split(REGEX_NEW_LINE))).stepsToTop();
    }

    @Override
    public Integer part2(String input) {
        return new Field(List.of(input.split(REGEX_NEW_LINE))).stepsToBottom();
    }

    static class Field {
        char[][] charField;
        int[][] stepField;
        Point startPoint;
        Point endPoint;

        public Field(List<String> strings) {
            int stringSize = strings.size();
            int stringLength = strings.get(0).length();
            this.charField = new char[stringSize][stringLength];
            this.stepField = new int[stringSize][stringLength];
            List<Point> startAndEndPoints = startAndEndPoints(strings, charField);
            this.startPoint = startAndEndPoints.get(0);
            this.endPoint = startAndEndPoints.get(1);
        }

        int stepsToTop() {
            List<Point> points = List.of(new Point(startPoint.x, startPoint.y));
            while (true) {
                points = check(charField, stepField, points,
                        (Point point, Point neighbor) -> charField[point.y][point.x] + 1 >= charField[neighbor.y][neighbor.x]);
                if (stepField[endPoint.y][endPoint.x] > 0)
                    return stepField[endPoint.y][endPoint.x];
            }
        }

        int stepsToBottom() {
            List<Point> points = List.of(new Point(endPoint.x, endPoint.y));
            while (true) {
                points = check(charField, stepField, points,
                        (Point point, Point neighbor) -> charField[point.y][point.x] - 1 <= charField[neighbor.y][neighbor.x]);
                for (Point point : points) {
                    if (charField[point.y][point.x] == 'a')
                        return stepField[point.y][point.x];
                }
            }
        }
    }

    static List<Point> startAndEndPoints(List<String> strings, char[][] charField) {
        Point startPoint = null;
        Point endPoint = null;
        for (int j = 0; j < strings.size(); j++) {
            String string = strings.get(j);
            for (int i = 0; i < string.length(); i++) {
                char currentChar = string.charAt(i);
                charField[j][i] = currentChar;
                if (currentChar == 'S') {
                    charField[j][i] = 'a';
                    startPoint = new Point(i, j);
                } else if (currentChar == 'E') {
                    charField[j][i] = 'z';
                    endPoint = new Point(i, j);
                }
            }
        }
        return List.of(Objects.requireNonNull(startPoint), Objects.requireNonNull(endPoint));
    }

    record Point(int x, int y) {
        List<Point> neighbors(int sizeX, int sizeY) {
            List<Point> neighbors = new ArrayList<>();
            if (x > 0) neighbors.add(new Point(x - 1, y));
            if (x < sizeX - 1) neighbors.add(new Point(x + 1, y));
            if (y > 0) neighbors.add(new Point(x, y - 1));
            if (y < sizeY - 1) neighbors.add(new Point(x, y + 1));
            return neighbors;
        }

    }

    private static List<Point> check(char[][] charField, int[][] stepField, List<Point> nowPoints,
                                     BiFunction<Point, Point, Boolean> check) {
        List<Point> newPoints = new ArrayList<>();
        for (Point point : nowPoints) {
            for (Point neighbor : point.neighbors(charField[0].length, charField.length)) {
                if (check.apply(point, neighbor) && stepField[neighbor.y][neighbor.x] == 0) {
                    stepField[neighbor.y][neighbor.x] = stepField[point.y][point.x] + 1;
                    newPoints.add(neighbor);
                }
            }
        }
        return newPoints;
    }
}
