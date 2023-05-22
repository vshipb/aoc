package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 implements Puzzle {
    @Override
    public String part1(String input) {
        List<Elf> elves = parseInput(input);
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
        List<Elf> elves = parseInput(input);
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
        Direction direction;
        boolean moved = false;
        Set<Point> points = elves.stream().map(e -> e.current).collect(Collectors.toSet());
        for (Elf elf : elves) {
            EnumMap<Direction, Boolean> isDirectionEmptyMap = new EnumMap<>(Direction.class);
            for (Direction d : Direction.values()) {
                isDirectionEmptyMap.put(d, elf.isDirectionEmpty(d, points));
            }
            direction = mainDirection;
            if ((isDirectionEmptyMap.values().stream().allMatch(b -> b))
                    || (isDirectionEmptyMap.values().stream().noneMatch(b -> b))) {
                elf.next = elf.current;
                continue;
            }
            moved = true;
            while (!isDirectionEmptyMap.get(direction)) {
                direction = direction.nextDirection();
            }
            elf.next = direction.getPoint(elf.current);
        }
        return !moved;
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

    private static List<Elf> parseInput(String input) {
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

    enum Direction {
        North(-1, 0), South(1, 0), West(0, -1), East(0, 1);
        final int dy;
        final int dx;

        Direction(int dy, int dx) {
            this.dy = dy;
            this.dx = dx;
        }

        public Point getPoint(Point p) {
            return new Point(p.y + dy, p.x + dx);
        }

        public Stream<Point> getPoints(Point p) {
            return dy == 0 ? Stream.of(new Point(p.y - 1, p.x + dx), new Point(p.y, p.x + dx), new Point(p.y + 1, p.x + dx))
                    : Stream.of(new Point(p.y + dy, p.x - 1), new Point(p.y + dy, p.x), new Point(p.y + dy, p.x + 1));
        }

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

        boolean isDirectionEmpty(Direction direction, Set<Point> points) {
            return direction.getPoints(current).noneMatch(points::contains);
        }
    }

    private record Point(int y, int x) {
    }
}
