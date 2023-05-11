package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Day23 implements Puzzle {
    @Override
    public String part1(String input) {
        List<Elf> elves = listOfElves(input);
        int mainDirection = 0;
        for (int i = 0; i < 10; i++) {
            firstHalf(elves, mainDirection);
            secondHalf(elves);
            mainDirection = nextDirection(mainDirection);
        }
        int x = elves.stream().mapToInt(elf -> elf.first.x).max().orElseThrow()
                - elves.stream().mapToInt(elf -> elf.first.x).min().orElseThrow() + 1;
        int y = elves.stream().mapToInt(elf -> elf.first.y).max().orElseThrow()
                - elves.stream().mapToInt(elf -> elf.first.y).min().orElseThrow() + 1;
        return String.valueOf((x * y) - elves.size());
    }

    @Override
    public String part2(String input) {
        List<Elf> elves = listOfElves(input);
        int mainDirection = 0;
        int i = 0;
        while (true) {
            i++;
            if (firstHalf(elves, mainDirection)) return String.valueOf(i);
            secondHalf(elves);
            mainDirection = nextDirection(mainDirection);
            if (i % 10 == 0) System.out.println(i);
        }
    }

    private static boolean firstHalf(List<Elf> elves, int mainDirection) {
        boolean north;
        boolean south;
        boolean west;
        boolean east;
        int direction;
        int countNoMove = 0;
        for (Elf elf : elves) {
            north = elf.northIsEmpty(elves);
            south = elf.southIsEmpty(elves);
            west = elf.westIsEmpty(elves);
            east = elf.eastIsEmpty(elves);
            direction = mainDirection;
            for (int j = 0; j < 4; j++) {
                if ((north && south && west && east) || (!north && !south && !west && !east)) {
                    elf.second = elf.first;
                    countNoMove++;
                    break;
                }
                elfMoves(direction, north, south, west, east, elf);
                if (elf.second != null) break;
                direction = nextDirection(direction);
            }
        }
        return (countNoMove == elves.size());
    }

    private static void secondHalf(List<Elf> elves) {
        List<Point> duplicates = elves.stream().map(elf -> elf.second)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                .filter(p -> p.getValue() > 1).map(Map.Entry::getKey).toList();
        for (Elf elf : elves) {
            if (!duplicates.contains(elf.second)) {
                elf.first = elf.second;
            }
            elf.second = null;
        }
    }

    private static void elfMoves(int direction, boolean north, boolean south, boolean west, boolean east, Elf elf) {
        int y = elf.first.y;
        int x = elf.first.x;
        switch (direction) {
            case 0 -> {
                if (north) {
                    elf.second = new Point(y - 1, x);
                }
            }
            case 1 -> {
                if (south) {
                    elf.second = new Point(y + 1, x);
                }
            }
            case 2 -> {
                if (west) {
                    elf.second = new Point(y, x - 1);
                }
            }
            case 3 -> {
                if (east) {
                    elf.second = new Point(y, x + 1);
                }
            }
            default -> throw new
                    IllegalArgumentException("index of direction " + direction + " is not valid");

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

    private static int nextDirection(int direction) {
        return  (direction + 1) % 4;
    }

    private static class Elf {
        Point first;
        Point second;
        public Elf(int y, int x) {
            this.first = new Point(y, x);
        }
        boolean northIsEmpty(List<Elf> elves) {
            int north = first.y - 1;
            int x = first.x;
            List<Point> points = List.of(new Point(north, x - 1), new Point(north, x), new Point(north, x + 1));
            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }

        boolean southIsEmpty(List<Elf> elves) {
            int south = first.y + 1;
            int x = first.x;
            List<Point> points = List.of(new Point(south, x - 1), new Point(south, x), new Point(south, x + 1));
            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }

        boolean westIsEmpty(List<Elf> elves) {
            int west = first.x - 1;
            int y = first.y;
            List<Point> points = List.of(new Point(y - 1, west), new Point(y, west), new Point(y + 1, west));
            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }

        boolean eastIsEmpty(List<Elf> elves) {
            int east = first.x + 1;
            int y = first.y;
            List<Point> points = List.of(new Point(y - 1, east), new Point(y, east), new Point(y + 1, east));
            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }
    }

    private record Point(int y, int x) {
    }
}
