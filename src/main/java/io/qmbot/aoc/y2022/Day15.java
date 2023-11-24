package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class Day15 implements Puzzle {
    @Override
    public Long part1(String input) {
        int min = 1500000;
        Field field = new Field(List.of(input.split(REGEX_NEW_LINE)), min);
        field.sensorsAreaAtString(min + 10);
        return Arrays.stream(field.seekingField).filter(value -> value == 1).count();
    }

    @Override
    public Long part2(String input) {
        Field field = new Field(List.of(input.split(REGEX_NEW_LINE)), 0);
        int max = 4000000;
        int seekingX = 0;
        List<Segment> segments = new ArrayList<>();
        Segment segment;
        Segment testing = new Segment(0, 0);
        Segment falseField = new Segment(0, max);
        for (int y = 0; y < max; y++) {
            segments.clear();
            segment = new Segment(0, 0);
            for (Point point : field.sensors) {
                Segment test = notFreeSpace(y, point, field.beacons.get(field.sensors.indexOf(point)), max);
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
        return 0L;
    }

    static class Field {
        List<Point> sensors = new ArrayList<>();
        List<Point> beacons = new ArrayList<>();
        int[] seekingField = new int[200000000];

        public Field(List<String> strings, int min) {
            int sensorX;
            int sensorY;
            int beaconX;
            int beaconY;
            int seekingY = min + 10;
            for (String string : strings) {
                String[] info = string.split(" ");
                sensorX = Integer.parseInt(info[2].substring(2, info[2].length() - 1)) + min;
                sensorY = Integer.parseInt(info[3].substring(2, info[3].length() - 1)) + min;
                beaconX = Integer.parseInt(info[8].substring(2, info[8].length() - 1)) + min;
                beaconY = Integer.parseInt(info[9].substring(2)) + min;
                this.sensors.add(new Point(sensorY, sensorX));
                this.beacons.add(new Point(beaconY, beaconX));
                if (seekingY == (sensorY)) {
                    this.seekingField[sensorX] = 8;
                }
                if (seekingY == (beaconY)) {
                    this.seekingField[beaconX] = 6;
                }
            }
        }

        void sensorsAreaAtString(int seekingY) {
            sensors.forEach(point -> sensorAreaAtString(seekingField, seekingY, point, beacons.get(sensors.indexOf(point))));
        }
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
            int x = taxicabDistance - Math.abs(seekingY - sensor.partOneY);
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
