package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;

public class Day12 implements Puzzle {
    @Override
    public Integer part1(String input) {
        List<String> strings = List.of(input.split("\n"));
        char[][] charField = new char[strings.size()][strings.get(0).length()];
        int[][] stepField = new int[strings.size()][strings.get(0).length()];
        Point startPoint = new Point(0, 0);
        Point endPoint = new Point(0, 0);
        for (int j = 0; j < strings.size(); j++) {
            String string = strings.get(j);
            for (int i = 0; i < string.length(); i++) {
                charField[j][i] = string.charAt(i);
                if (charField[j][i] == 'S') {
                    charField[j][i] = 'a';
                    startPoint = new Point(i, j);
                    stepField[j][i] = 0;
                }
                if (charField[j][i] == 'E') {
                    charField[j][i] = 'z';
                    endPoint = new Point(i, j);
                }
            }
        }
        List<Point> points = List.of(new Point(startPoint.x, startPoint.y));
        int neighborCounts = 1;
        while (neighborCounts != 0) {
            points = fromBottomToTopCheck(charField, stepField, points);
            neighborCounts = points.size();
        }
        return stepField[endPoint.y][endPoint.x];
    }

    @Override
    public Integer part2(String input) {
        List<String> strings = List.of(input.split("\n"));
        int stringSize = strings.size();
        int stringSize1 = strings.get(0).length();
        char[][] charField = new char[stringSize][stringSize1];
        int[][] stepField = new int[stringSize][stringSize1];
        Point endPoint = new Point(0, 0);
        for (int j = 0; j < strings.size(); j++) {
            String string = strings.get(j);
            for (int i = 0; i < string.length(); i++) {
                charField[j][i] = string.charAt(i);
                if (charField[j][i] == 'S') {
                    charField[j][i] = 'a';
                }
                if (charField[j][i] == 'E') {
                    charField[j][i] = 'z';
                    endPoint = new Point(i, j);
                }
            }
        }
        List<Point> points = List.of(new Point(endPoint.x, endPoint.y));
        while (charField[points.get(0).y][points.get(0).x] != 'a') {
            points = fromTopToBottomCheck(charField, stepField, points);
        }
        return stepField[points.get(0).y][points.get(0).x];
    }

    record Point(int x, int y) {

        List<Point> neighbors(int sizeX, int sizeY) {
            List<Point> neighbors = new ArrayList<>();
            if (x > 0) {
                neighbors.add(new Point(x - 1, y));
            }
            if (x < sizeX - 1) {
                neighbors.add(new Point(x + 1, y));
            }
            if (y > 0) {
                neighbors.add(new Point(x, y - 1));
            }
            if (y < sizeY - 1) {
                neighbors.add(new Point(x, y + 1));
            }
            return neighbors;
        }

    }

    private static List<Point> fromBottomToTopCheck(char[][] charField, int[][] stepField, List<Point> nowPoints) {
        List<Point> newPoints = new ArrayList<>();
        for (Point point : nowPoints) {
            for (Point neighbor : point.neighbors(charField[0].length, charField.length)) {
                if (charField[point.y][point.x] + 1 >= charField[neighbor.y][neighbor.x]
                        && stepField[neighbor.y][neighbor.x] == 0) {
                    stepField[neighbor.y][neighbor.x] = stepField[point.y][point.x] + 1;
                    newPoints.add(neighbor);
                }
            }
        }
        return newPoints;
    }

    private static List<Point> fromTopToBottomCheck(char[][] charField, int[][] stepField, List<Point> nowPoints) {
        List<Point> newPoints = new ArrayList<>();
        for (Point point : nowPoints) {
            for (Point neighbor : point.neighbors(charField[0].length, charField.length)) {
                if (charField[point.y][point.x] - 1 <= charField[neighbor.y][neighbor.x]
                        && stepField[neighbor.y][neighbor.x] == 0) {
                    stepField[neighbor.y][neighbor.x] = stepField[point.y][point.x] + 1;
                    newPoints.add(neighbor);
                    if (charField[neighbor.y][neighbor.x] == 'a') {
                        newPoints.clear();
                        newPoints.add(neighbor);
                        return newPoints;
                    }
                }
            }
        }
        return newPoints;
    }
}
