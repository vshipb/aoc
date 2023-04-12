package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Day17 implements Puzzle {
    @Override
    public String part1(String input) {
        LimitedQueue<String[]> field = new LimitedQueue<>(100);
        String trimmedInput = input.trim();
        AtomicInteger added = new AtomicInteger(0);

        for (int i = 0; i < 4; i++) {
            String[] line = new String[7];
            Arrays.fill(line, "0");
            field.addLine(line);
            added.getAndIncrement();
        }

        int startDirection = 0;
        int high;

        for (int i = 0; i < 2022; i++) {
            startDirection = createFigure(i, field, added).startFalling(field, trimmedInput, startDirection);
        }

        high = added.intValue() - (newStartY(field) + 4);
        return String.valueOf(high);
    }

    @Override
    public String part2(String input) {
        LimitedQueue<String[]> field = new LimitedQueue<>(100);
        String trimmedInput = input.trim();
        AtomicInteger added = new AtomicInteger(0);

        for (int i = 0; i < 4; i++) {
            String[] line = new String[7];
            Arrays.fill(line, "0");
            field.addLine(line);
            added.getAndIncrement();
        }

        int startDirection = 0;
        int high;

        for (long i = 0; i < 1_000_000_000_000L; i++) {
            startDirection = createFigure(i, field, added).startFalling(field, trimmedInput, startDirection);
        }

        high = added.intValue() - (newStartY(field) + 4);
        return String.valueOf(high);
    }

    private static Figure createFigure(long step, LimitedQueue<String[]> field, AtomicInteger removed) {
        Figure figure;
        int indexF = (int) (step % 5);

        switch (indexF) {
            case 0 -> {
                fillField(field, 1, removed);
                figure = horizontal(newStartY(field));
            }
            case 1 -> {
                fillField(field, 3, removed);
                figure = plus(newStartY(field));
            }
            case 2 -> {
                fillField(field, 3, removed);
                figure = angle(newStartY(field));
            }
            case 3 -> {
                fillField(field, 4, removed);
                figure = vertical(newStartY(field));
            }
            case 4 -> {
                fillField(field, 2, removed);
                figure = square(newStartY(field));
            }
            default -> throw new IllegalArgumentException("Figure index " + indexF + " is not valid");
        }

        return figure;
    }

    private static int newStartY(LimitedQueue<String[]> field) {
        for (int i = 0; i < field.size(); i++) {
            if (Arrays.asList(field.get(i)).contains("7")) {
                return i - 4;
            }
        }
        return 0;
    }

    private static void fillField(LimitedQueue<String[]> field, int figureHeight, AtomicInteger removed) {
        int plusPole;
        int fix = newStartY(field);

        plusPole = figureHeight - fix - 1;

        for (int i = 0; i < plusPole; i++) {
            String[] line = new String[7];
            Arrays.fill(line, "0");
            field.addLine(line);
            removed.getAndIncrement();
        }
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


        int startFalling(LimitedQueue<String[]> field, String jetPattern, int jetPatternPosition) {
            boolean spaceForFalling = true;
            while (spaceForFalling) {
                boolean space = true;

                for (Point point : figurePoints) {
                    field.get(point.pointY)[point.pointX] = "7";
                }

                for (Point point : figurePoints) {
                    field.get(point.pointY)[point.pointX] = "0";
                }

                switch (jetPattern.charAt(jetPatternPosition)) {
                    case '<' -> {
                        for (Point point : figurePoints) {
                            if (point.pointX == 0 || !field.get(point.pointY)[point.pointX - 1].equals("0")) {
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
                            if (point.pointX == 6 || !field.get(point.pointY)[point.pointX + 1].equals("0")) {
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
                            + jetPattern.charAt(jetPatternPosition) + " is not valid");
                }
                jetPatternPosition++;
                if (jetPatternPosition == jetPattern.length()) jetPatternPosition = 0;

                for (Point point : figurePoints) {
                    if (point.pointY + 1 == field.size() || field.get(point.pointY + 1)[point.pointX].equals("7")) {
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
                    field.get(point.pointY)[point.pointX] = "7";
                }
            }
            return jetPatternPosition;
        }
    }

    static Figure horizontal(int startY) {
        return new Figure(new Point(startY, 2), new Point(startY, 3), new Point(startY, 4),
                new Point(startY, 5));
    }

    static Figure plus(int startY) {
        return new Figure(new Point(startY, 3), new Point(startY - 1, 3), new Point(startY - 2, 3),
                new Point(startY - 1, 2), new Point(startY - 1, 4));
    }

    static Figure angle(int startY) {
        return new Figure(new Point(startY, 4), new Point(startY - 1, 4), new Point(startY - 2, 4),
                new Point(startY, 3), new Point(startY, 2));
    }

    static Figure vertical(int startY) {
        return new Figure(new Point(startY, 2), new Point(startY - 1, 2), new Point(startY - 2, 2),
                new Point(startY - 3, 2));
    }

    static Figure square(int startY) {
        return new Figure(new Point(startY, 2), new Point(startY, 3), new Point(startY - 1, 2),
                new Point(startY - 1, 3));
    }

    public static class LimitedQueue<E> extends LinkedList<E> {
        private int limit;

        public LimitedQueue(int limit) {
            this.limit = limit;
        }

        public boolean addLine(E o) {
            super.addFirst(o);
            while (size() > limit) {
                super.removeLast();
            }
            return true;
        }
    }
}
