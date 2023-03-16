package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;

public class Day12 implements Puzzle {
    @Override
    public String part1(String input) {
        List<String> strings = List.of(input.split("\n"));
        char[][] field = new char[strings.size()][strings.get(0).length()];
        int[][] field1 = new int[strings.size()][strings.get(0).length()];
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;
        for (int j = 0; j < strings.size(); j++) {
            String string = strings.get(j);
            for (int i = 0; i < string.length(); i++) {
                field[j][i] = string.charAt(i);
                if (field[j][i] == 'S') {
                    field[j][i] = 'a';
                    startY = j;
                    startX = i;
                    field1[startY][startX] = 0;
                }
                if (field[j][i] == 'E') {
                    field[j][i] = 'z';
                    endX = i;
                    endY = j;
                }
            }
        }
        List<Point> points = List.of(new Point(startX, startY));
        int neighborCounts = 1;
        while (neighborCounts != 0) {
            points = newPointsCheck(field, field1, points);
            neighborCounts = points.size();
        }
        return Integer.toString(field1[endY][endX]);
    }

    @Override
    public String part2(String input) {
        List<String> strings = List.of(input.split("\n"));
        int stringSize = strings.size();
        int stringSize1 = strings.get(0).length();
        char[][] field = new char[stringSize][stringSize1];
        int[][] field1 = new int[stringSize][stringSize1];
        int endX = 0;
        int endY = 0;
        for (int j = 0; j < strings.size(); j++) {
            String string = strings.get(j);
            for (int i = 0; i < string.length(); i++) {
                field[j][i] = string.charAt(i);
                if (field[j][i] == 'S') {
                    field[j][i] = 'a';
                }
                if (field[j][i] == 'E') {
                    field[j][i] = 'z';
                    endX = i;
                    endY = j;
                }
            }
        }
        List<Point> points = List.of(new Point(endX, endY));
        while (field[points.get(0).y][points.get(0).x] != 'a') {
            points = newPointsCheck1(field, field1, points);
        }
        return Integer.toString(field1[points.get(0).y][points.get(0).x]);
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

    private static List<Point> newPointsCheck(char[][] field, int[][] field1, List<Point> nowPoints) {
        List<Point> newPoints = new ArrayList<>();
        List<Point> neighbors1;
        for (Point point : nowPoints) {
            neighbors1 = point.neighbors(field[0].length, field.length);
            for (Point neighbor : neighbors1) {
                if (field[point.y][point.x] + 1 >= field[neighbor.y][neighbor.x]
                        && field1[neighbor.y][neighbor.x] == 0) {
                    field1[neighbor.y][neighbor.x] = field1[point.y][point.x] + 1;
                    newPoints.add(neighbor);
                }
            }
        }
        return newPoints;
    }

    private static List<Point> newPointsCheck1(char[][] field, int[][] field1, List<Point> nowPoints) {
        List<Point> newPoints = new ArrayList<>();
        List<Point> neighbors1;
        for (Point point : nowPoints) {
            neighbors1 = point.neighbors(field[0].length, field.length);
            for (Point neighbor : neighbors1) {
                if (field[point.y][point.x] - 1 <= field[neighbor.y][neighbor.x]
                        && field1[neighbor.y][neighbor.x] == 0) {
                    field1[neighbor.y][neighbor.x] = field1[point.y][point.x] + 1;
                    newPoints.add(neighbor);
                    if (field[neighbor.y][neighbor.x] == 'a') {
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
