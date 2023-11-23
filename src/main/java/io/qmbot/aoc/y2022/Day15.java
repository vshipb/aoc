package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day15 implements Puzzle {
    @Override
    public Long part1(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
        int sensorX;
        int sensorY;
        int beaconX;
        int beaconY;
        int min = 1500000;
        int seekingY = min + 10;
        List<Point> sensors = new ArrayList<>();
        List<Point> beacons = new ArrayList<>();
        int[] seekingField = new int[200000000];
        for (String string : strings) {
            String[] info = string.split(" ");
            sensorX = Integer.parseInt(info[2].substring(2, info[2].length() - 1)) + min;
            sensorY = Integer.parseInt(info[3].substring(2, info[3].length() - 1)) + min;
            beaconX = Integer.parseInt(info[8].substring(2, info[8].length() - 1)) + min;
            beaconY = Integer.parseInt(info[9].substring(2)) + min;
            sensors.add(new Point(sensorY, sensorX));
            beacons.add(new Point(beaconY, beaconX));
            if (seekingY == (sensorY)) {
                seekingField[sensorX] = 8;
            }
            if (seekingY == (beaconY)) {
                seekingField[beaconX] = 6;
            }
        }
        List<Segment> segments = new ArrayList<>();
        Segment segment = new Segment(0, 0);
        int max = 200000000;
        for (Point point : sensors) {
            Segment test = notFreeSpace(seekingY, point, beacons.get(sensors.indexOf(point)), max);
            segments.add(test);
        }
        Collections.sort(segments);
        for (Segment point : segments) {
            segment = fuse(point, segment);
        }
        long result = segment.second - segment.first;
        result = result - min;
//        for (Point point : sensors) {
//            sensorAreaAtString(seekingField, seekingY, point, beacons.get(sensors.indexOf(point)));
//        }
//        long freeSpace1 = 0;
//        for (int j : seekingField) {
//            if (j == 1) freeSpace1++;
//        }
        return result;
    }

    @Override
    public Long part2(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
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
        List<Segment> segments = new ArrayList<>();
        Segment segment;
        Segment testing = new Segment(0, 0);
        Segment falseField = new Segment(0, max);
        for (int y = 0; y < max; y++) {
            segments.clear();
            segment = new Segment(0, 0);
            for (Point point : sensors) {
                Segment test = notFreeSpace(y, point, beacons.get(sensors.indexOf(point)), max);
                if (!test.equals(testing)) {
                    segments.add(test);
                }
            }
            Collections.sort(segments);
            for (Segment point : segments) {
                segment = fuse(segment, point);
                if (segment.equals(falseField)) break;
                if (segment.equals(testing)) {
                    return seekingX * 4000000L + y;
                }
                seekingX = segment.second + 1;
            }
        }
        return null;
    }


    static class Segment implements Comparable<Segment> {
        private final int first;
        private final int second;

        public Segment(int first, int second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int compareTo(@NotNull Segment o) {
            return Integer.compare(this.first, o.first);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Segment segment = (Segment) o;
            if (first != segment.first) return false;
            return second == segment.second;
        }

        @Override
        public String toString() {
            return first +
                    ", " + second;
        }
    }

    static class Point {
        int partOneY;
        int partTwoX;

        public Point(int y, int x) {
            this.partOneY = y;
            this.partTwoX = x;
        }
    }

    private static Segment fuse(Segment a, Segment b) {
        return (a.second >= b.first - 1) ? new Segment(Math.min(a.first, b.first), Math.max(a.second, b.second)) : new Segment(0, 0);
    }

    private static void sensorAreaAtString(int[] field, int seekingY, Point sensor, Point beacon) {
        int taxicabDistance = Math.abs(sensor.partTwoX - beacon.partTwoX) + Math.abs(sensor.partOneY - beacon.partOneY);
        if (sensor.partOneY - taxicabDistance <= seekingY && seekingY <= sensor.partOneY + taxicabDistance) {
            int x = Math.abs(seekingY - sensor.partOneY);
            x = taxicabDistance - x;
            for (int i = sensor.partTwoX - x; i <= sensor.partTwoX + x; i++) {
                if (field[i] == 0) field[i] = 1;
            }
        }
    }

    private static Segment notFreeSpace(int seekingY, Point sensor, Point beacon, int max) {
        int taxicabDistance = Math.abs(sensor.partTwoX - beacon.partTwoX) + Math.abs(sensor.partOneY - beacon.partOneY);
        int first = 0;
        int second = 0;
        if (sensor.partOneY - taxicabDistance <= seekingY && seekingY <= sensor.partOneY + taxicabDistance) {
            int notFree = Math.abs(seekingY - sensor.partOneY);
            notFree = taxicabDistance - notFree;
            first = sensor.partTwoX - notFree;
            if (first < 0) first = 0;
            second = sensor.partTwoX + notFree;
            if (second > max) second = max;
        }
        return new Segment(first, second);
    }
}
