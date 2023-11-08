package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.LongFunction;

public class Day15 implements Puzzle {
    @Override
    public Long part1(String input) {
        List<String> strings = List.of(input.split("\n"));
        int sensorX;
        int sensorY;
        int beaconX;
        int beaconY;
        int minX = 1500000;
        int minY = 1500000;
        int seekingY = minY + 10;
        List<Point> sensors = new ArrayList<>();
        List<Point> beacons = new ArrayList<>();
        int[] seekingField = new int[200000000];
        for (String string : strings) {
            String[] info = string.split(" ");
            sensorX = Integer.parseInt(info[2].substring(2, info[2].length() - 1));
            sensorY = Integer.parseInt(info[3].substring(2, info[3].length() - 1));
            beaconX = Integer.parseInt(info[8].substring(2, info[8].length() - 1));
            beaconY = Integer.parseInt(info[9].substring(2));
            sensors.add(new Point(sensorY + minY, sensorX + minX));
            beacons.add(new Point(beaconY + minY, beaconX + minX));
            if (seekingY == (sensorY + minY)) {
                seekingField[sensorX + minX] = 8;
            }
            if (seekingY == (beaconY + minY)) {
                seekingField[beaconX + minX] = 6;
            }
        }
        for (Point point : sensors) {
            sensorAreaAtString(seekingField, seekingY, point, beacons.get(sensors.indexOf(point)));
        }
        long freeSpace1 = 0;
        for (int j : seekingField) {
            if (j == 1) freeSpace1++;
        }
        return freeSpace1;
    }

    @Override
    public Long part2(String input) {
        List<String> strings = List.of(input.split("\n"));
        int sensorX;
        int sensorY;
        int beaconX;
        int beaconY;
        int max = 4000000;
        List<Point> sensors = new ArrayList<>();
        List<Point> beacons = new ArrayList<>();

        for (String string : strings) {
            String[] info = string.split(" ");
            sensorX = Integer.parseInt(info[2].substring(2, info[2].length() - 1));
            sensorY = Integer.parseInt(info[3].substring(2, info[3].length() - 1));
            beaconX = Integer.parseInt(info[8].substring(2, info[8].length() - 1));
            beaconY = Integer.parseInt(info[9].substring(2));
            sensors.add(new Point(sensorY, sensorX));
            beacons.add(new Point(beaconY, beaconX));
        }
        int seekingX = 0;
        int seekingY;
        List<Point> pointsA = new ArrayList<>();
        Point pointB;

        Point testing = new Point(0, 0);
        Point falseField = new Point(0, max);
        for (int j = 0; j < max; j++) {
            pointsA.clear();
            pointB = new Point(0, 0);
            seekingY = j;

            for (Point point : sensors) {
                Point test = notFreeSpace(seekingY, point, beacons.get(sensors.indexOf(point)), max);
                if (!test.equals(testing)) {
                    pointsA.add(test);
                }
            }
            Collections.sort(pointsA);
            for (Point point : pointsA) {
                pointB = fuse(pointB, point);
                if (pointB.equals(falseField)) break;
                if (pointB.equals(testing)) {
                    return seekingX * 4000000L + j;
                }
                seekingX = pointB.partTwoX + 1;
            }
        }
        return null;
    }

    static class Point implements Comparable<Point> {
        int partOneY;
        int partTwoX;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (partOneY != point.partOneY) return false;
            return partTwoX == point.partTwoX;
        }

        @Override
        public int hashCode() {
            int result = partOneY;
            result = 31 * result + partTwoX;
            return result;
        }

        public Point(int y, int x) {
            this.partOneY = y;
            this.partTwoX = x;
        }

        @Override
        public int compareTo(Point o) {
            return Integer.compare(this.partOneY, o.partOneY);
        }
    }

    private static Point fuse(Point a, Point b) {
        if (a.partTwoX >= b.partOneY - 1) {
            return new Point(Math.min(a.partOneY, b.partOneY), Math.max(a.partTwoX, b.partTwoX));
        } else {
            return new Point(0, 0);
        }
    }

    private static void sensorAreaAtString(int[] field, int seekingY, Point sensor, Point beacon) {
        int taxicabDistance = Math.abs(sensor.partTwoX - beacon.partTwoX) + Math.abs(sensor.partOneY - beacon.partOneY);
        int minY = sensor.partOneY - taxicabDistance;
        int maxY = sensor.partOneY + taxicabDistance;
        if (minY <= seekingY && seekingY <= maxY) {
            int x = Math.abs(seekingY - sensor.partOneY);
            x = taxicabDistance - x;
            for (int i = sensor.partTwoX - x; i <= sensor.partTwoX + x; i++) {
                if (field[i] == 0) field[i] = 1;
            }
        }
    }

    private static Point notFreeSpace(int seekingY, Point sensor, Point beacon, int max) {
        int taxicabDistance = Math.abs(sensor.partTwoX - beacon.partTwoX) + Math.abs(sensor.partOneY - beacon.partOneY);
        int minY = sensor.partOneY - taxicabDistance;
        int maxY = sensor.partOneY + taxicabDistance;
        int y = 0;
        int y1 = 0;
        if (minY <= seekingY && seekingY <= maxY) {
            int notFree = Math.abs(seekingY - sensor.partOneY);
            notFree = taxicabDistance - notFree;
            y = sensor.partTwoX - notFree;
            y1 = sensor.partTwoX + notFree;
            if (y < 0) y = 0;
            if (y1 > max) y1 = max;
        }
        return new Point(y, y1);
    }
}
