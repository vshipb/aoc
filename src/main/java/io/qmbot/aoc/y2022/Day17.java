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
        Field field = new Field(input, 8000, 2022);
        field.falling(field.countFigures);
        int high = field.added.intValue() - (newStartY(field.field) + 4);
        return String.valueOf(high);
    }

    @Override
    public String part2(String input) {
        Field f = new Field(input, 100, 10000);
        f.falling(f.countFigures);
        long iFirst = f.figuresBeforeCycle;
        Field field = new Field(input, 100, 1_000_000_000_000L);
        field.countCycles(iFirst, f.figuresForCycle);
        field.falling(field.figuresLeftAfterCycles + iFirst);
        return String.valueOf(field.findY(f.stringsForCycle));
    }

    static class Field {
        long cyclesCount;
        long figuresLeftAfterCycles;
        LimitedQueue<String[]> field;
        String trimmedInput;
        AtomicInteger added = new AtomicInteger(0);
        long countFigures;
        int jetPatternPosition = 0;
        int stringsBeforeCycle;
        long stringsForCycle;
        long figuresBeforeCycle = 0;
        long figuresForCycle = 0;
        String orient = "";

        void falling(long count) {
            for (long i = 0; i < count; i++) {
                startFallingField(createFigure(i, field, added), i, added);
            }
        }

        long findY(long aS) {
            int high = added.intValue() - (newStartY(field) + 4);
            cyclesCount = (cyclesCount * aS);
            return cyclesCount + high;
        }

        void countCycles(long figureNumberFirst, long figureNumberSecond) {
            long figures = countFigures - figureNumberFirst;
            cyclesCount = figures / figureNumberSecond;
            figuresLeftAfterCycles = figures % figureNumberSecond;
        }

        void startFallingField(Figure figure, long i, AtomicInteger added) {
            boolean spaceForFalling = true;
            while (spaceForFalling) {
                figure.moveX(trimmedInput.charAt(jetPatternPosition), field);
                jetPatternPosition++;
                if (jetPatternPosition == trimmedInput.length()) {
                    jetPatternPosition = 0;
                    if (orient.isEmpty()) {
                        orient = figure.name;
                    } else if (orient.equals(figure.name) && figuresBeforeCycle == 0) {
                        figuresBeforeCycle = i;
                        stringsBeforeCycle = added.intValue();
                    } else if (orient.equals(figure.name) && figuresForCycle == 0) {
                        figuresForCycle = i - figuresBeforeCycle;
                        stringsForCycle = added.intValue() - stringsBeforeCycle;
                    }
                }
                spaceForFalling = figure.canMoveY(field);
                if (spaceForFalling) {
                    figure.moveY();
                }
            }
            figure.draw(field);
        }

        public Field(String input, int queueSize, long countFigures) {
            this.field = new LimitedQueue<>(queueSize);
            this.trimmedInput = input.trim();
            startFill(field, added);
            this.countFigures = countFigures;
        }
    }

    private static void startFill(LimitedQueue<String[]> field, AtomicInteger added) {
        for (int i = 0; i < 4; i++) {
            String[] line = new String[7];
            Arrays.fill(line, "0");
            field.addLine(line);
            added.getAndIncrement();
        }
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
        return field.size() - 4;
    }

    private static void fillField(LimitedQueue<String[]> field, int figureHeight, AtomicInteger added) {
        int plusPole = figureHeight - newStartY(field) - 1;
        for (int i = 0; i < plusPole; i++) {
            String[] line = new String[7];
            Arrays.fill(line, "0");
            field.addLine(line);
            added.getAndIncrement();
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
        String name;
        List<Point> figurePoints = new ArrayList<>();
        boolean space = true;

        public Figure(String name, Point... points) {
            this.name = name;
            figurePoints.addAll(Arrays.asList(points));
        }

        void draw(LimitedQueue<String[]> field) {
            for (Point point : figurePoints) {
                field.get(point.pointY)[point.pointX] = "7";
            }
        }

        void moveY() {
            for (Point point : figurePoints) {
                point.pointY = point.pointY + 1;
            }
        }

        boolean canMoveY(LimitedQueue<String[]> field) {
            for (Point point : figurePoints) {
                if (point.pointY + 1 == field.size() || field.get(point.pointY + 1)[point.pointX].equals("7")) {
                    return false;
                }
            }
            return true;
        }

        void moveX(char direction, LimitedQueue<String[]> field) {
            int deltaX = (direction == '<') ? -1 : 1;
            space = true;
            for (Point point : figurePoints) {
                int newX = point.pointX + deltaX;
                if (newX < 0 || newX > 6 || !field.get(point.pointY)[newX].equals("0")) {
                    space = false;
                    break;
                }
            }
            if (space) {
                for (Point point : figurePoints) {
                    point.pointX += deltaX;
                }
            }
        }
    }

    static Figure horizontal(int startY) {
        return new Figure("horizontal", new Point(startY, 2), new Point(startY, 3), new Point(startY, 4),
                new Point(startY, 5));
    }

    static Figure plus(int startY) {
        return new Figure("plus", new Point(startY, 3), new Point(startY - 1, 3), new Point(startY - 2, 3),
                new Point(startY - 1, 2), new Point(startY - 1, 4));
    }

    static Figure angle(int startY) {
        return new Figure("angle", new Point(startY, 4), new Point(startY - 1, 4), new Point(startY - 2, 4),
                new Point(startY, 3), new Point(startY, 2));
    }

    static Figure vertical(int startY) {
        return new Figure("vertical", new Point(startY, 2), new Point(startY - 1, 2), new Point(startY - 2, 2),
                new Point(startY - 3, 2));
    }

    static Figure square(int startY) {
        return new Figure("square", new Point(startY, 2), new Point(startY, 3), new Point(startY - 1, 2),
                new Point(startY - 1, 3));
    }

    public static class LimitedQueue<E> extends LinkedList<E> {
        private final int limit;

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
