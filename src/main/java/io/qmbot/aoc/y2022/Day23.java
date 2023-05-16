package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 implements Puzzle {
    @Override
    public String part1(String input) {
        List<Elf> elves = listOfElves(input);
        Direction mainDirection = Direction.North;
        for (int i = 0; i < 10; i++) {
            firstHalf(elves, mainDirection);
            secondHalf(elves);
            mainDirection = mainDirection.nextDirection();
        }
        int x = elves.stream().mapToInt(elf -> elf.current.x).max().orElseThrow()
                - elves.stream().mapToInt(elf -> elf.current.x).min().orElseThrow() + 1;
        int y = elves.stream().mapToInt(elf -> elf.current.y).max().orElseThrow()
                - elves.stream().mapToInt(elf -> elf.current.y).min().orElseThrow() + 1;
        return String.valueOf((x * y) - elves.size());
    }

    @Override
    public String part2(String input) {
        List<Elf> elves = listOfElves(input);
        Direction mainDirection = Direction.North;
        int i = 0;
        while (true) {
            i++;
            if (firstHalf(elves, mainDirection)) return String.valueOf(i);
            secondHalf(elves);
            mainDirection = mainDirection.nextDirection();
        }
    }

    private static boolean firstHalf(List<Elf> elves, Direction mainDirection) {
        boolean north;
        boolean south;
        boolean west;
        boolean east;
        Direction direction;
        int countNoMove = 0;
        Set<Point> points = elves.stream().map(e -> e.current).collect(Collectors.toSet());
        for (Elf elf : elves) {
            north = elf.directionIsEmpty(Direction.North, points);
            south = elf.directionIsEmpty(Direction.South, points);
            west = elf.directionIsEmpty(Direction.West, points);
            east = elf.directionIsEmpty(Direction.East, points);
            direction = mainDirection;
            for (int j = 0; j < 4; j++) {
                if ((north && south && west && east) || (!north && !south && !west && !east)) {
                    elf.next = elf.current;
                    countNoMove++;
                    break;
                }
                elfMoves(direction, north, south, west, east, elf);
                if (elf.next != null) break;
                direction = direction.nextDirection();
            }
        }
        return (countNoMove == elves.size());
    }

    private static void secondHalf(List<Elf> elves) {
        List<Point> duplicates = elves.stream().map(elf -> elf.next)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                .filter(p -> p.getValue() > 1).map(Map.Entry::getKey).toList();
        for (Elf elf : elves) {
            if (!duplicates.contains(elf.next)) {
                elf.current = elf.next;
            }
            elf.next = null;
        }
    }

    private static void elfMoves(Direction dir, boolean north, boolean south, boolean west, boolean east, Elf elf) {
        Point point = elf.current;
        switch (dir) {
            case  North -> {
                if (north) {
                    elf.next = dir.getPoint(point);
                }
            }
            case South -> {
                if (south) {
                    elf.next = dir.getPoint(point);
                }
            }
            case West -> {
                if (west) {
                    elf.next = dir.getPoint(point);
                }
            }
            case East -> {
                if (east) {
                    elf.next = dir.getPoint(point);
                }
            }
            default -> throw new
                    IllegalArgumentException("index of direction " + dir + " is not valid");

        }
    }

    private static List<Elf> listOfElves(String input) {
        String[] splitInput = input.split("\n");
        List<Elf> elves = new ArrayList<>();
        for (int i = 0; i < splitInput.length; i++) {
            for (int j = 0; j < splitInput[0].length(); j++) {
                if (splitInput[i].charAt(j) == '#') {
                    elves.add(new Elf(i, j));
                }
            }
        }
        return elves;
    }

    interface Dir {
        Point getPoint(Point p);

        Stream<Point> getPoints(Point p);
    }

    enum Direction implements Dir {
        North {
            @Override
            public Point getPoint(Point p) {
                return new Point(p.y - 1, p.x);
            }

            @Override
            public Stream<Point> getPoints(Point p) {
                return Stream.of(new Point(p.y - 1, p.x - 1), new Point(p.y - 1, p.x), new Point(p.y - 1, p.x + 1));
            }
        }, South {
            @Override
            public Point getPoint(Point p) {
                return new Point(p.y + 1, p.x);
            }

            @Override
            public Stream<Point> getPoints(Point p) {
                return Stream.of(new Point(p.y + 1, p.x - 1), new Point(p.y + 1, p.x), new Point(p.y + 1, p.x + 1));
            }
        }, West {
            @Override
            public Point getPoint(Point p) {
                return new Point(p.y, p.x - 1);
            }

            @Override
            public Stream<Point> getPoints(Point p) {
                return Stream.of(new Point(p.y - 1, p.x - 1), new Point(p.y, p.x - 1), new Point(p.y + 1, p.x - 1));
            }
        }, East {
            @Override
            public Point getPoint(Point p) {
                return new Point(p.y, p.x + 1);
            }

            @Override
            public Stream<Point> getPoints(Point p) {
                return Stream.of(new Point(p.y - 1, p.x + 1), new Point(p.y, p.x + 1), new Point(p.y + 1, p.x + 1));
            }
        };
        public Direction nextDirection() {
            return Direction.values()[(ordinal() + 1) % 4];
        }
    }

    private static class Elf {
        Point current;
        Point next;

        public Elf(int y, int x) {
            this.current = new Point(y, x);
        }

        boolean directionIsEmpty(Direction direction, Set<Point> points) {
            return direction.getPoints(current).noneMatch(points::contains);
        }
    }

    private record Point(int y, int x) {
    }
}
