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
        List<Elv> elves = listOfElves(input.split("\n"));

        boolean north;
        boolean south;
        boolean west;
        boolean east;

        int direction;
        int mainDirection = 0;

        for (int i = 0; i < 10; i++) {
            for (Elv elf : elves) {
                north = elf.northIsEmpty(elves);
                south = elf.southIsEmpty(elves);
                west = elf.westIsEmpty(elves);
                east = elf.eastIsEmpty(elves);

                direction = mainDirection;

                for (int j = 0; j < 4; j++) {
                    if ((north && south && west && east) || (!north && !south && !west && !east)) {
                        elf.second = elf.first;
                        break;
                    }

                    elfMoves(direction, north, south, west, east, elf);

                    if (elf.second != null) break;

                    direction = direction(direction);
                }
            }

            duplicates(elves);

            mainDirection = direction(mainDirection);
        }

        int x = elves.stream().mapToInt(elv -> elv.first.x).max().orElseThrow()
                - elves.stream().mapToInt(elv -> elv.first.x).min().orElseThrow() + 1;
        int y = elves.stream().mapToInt(elv -> elv.first.y).max().orElseThrow()
                - elves.stream().mapToInt(elv -> elv.first.y).min().orElseThrow() + 1;

        return String.valueOf((x * y) - elves.size());
    }

    @Override
    public String part2(String input) {
        List<Elv> elves = listOfElves(input.split("\n"));

        boolean north;
        boolean south;
        boolean west;
        boolean east;

        int direction;
        int mainDirection = 0;
        int countNoMove = 0;
        int i = 0;

        while (true) {
            i++;
            for (Elv elf : elves) {
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

                    direction = direction(direction);
                }
            }

            duplicates(elves);

            mainDirection = direction(mainDirection);

            if (i % 10 == 0) System.out.println(i);

            if (countNoMove == elves.size()) return String.valueOf(i);
            countNoMove = 0;
        }
    }

    private static void duplicates(List<Elv> elves) {
        List<Point> duplicates = elves.stream().map(elv -> elv.second)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                .filter(p -> p.getValue() > 1).map(Map.Entry::getKey).toList();

        for (Elv elv : elves) {
            if (!duplicates.contains(elv.second)) {
                elv.first = elv.second;
            }
            elv.second = null;
        }
    }

    private static void elfMoves(int direction, boolean north, boolean south, boolean west, boolean east, Elv elf) {
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

    private static List<Elv> listOfElves(String[] splitInput) {
        List<Elv> elves = new ArrayList<>();

        for (int i = 0; i < splitInput.length; i++) {
            for (int j = 0; j < splitInput[0].length(); j++) {
                if (splitInput[i].charAt(j) == '#') {
                    elves.add(new Elv(i, j));
                }
            }
        }

        return elves;
    }

    private static int direction(int direction) {
        if (direction == 3) {
            return 0;
        } else {
            return direction + 1;
        }
    }

    private static class Elv {
        Point first;
        Point second;

        public Elv(int y, int x) {
            this.first = new Point(y, x);
        }

        boolean northIsEmpty(List<Elv> elves) {
            int north = first.y - 1;
            int x = first.x;

            List<Point> points = List.of(new Point(north, x - 1), new Point(north, x), new Point(north, x + 1));

            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }

        boolean southIsEmpty(List<Elv> elves) {
            int south = first.y + 1;
            int x = first.x;


            List<Point> points = List.of(new Point(south, x - 1), new Point(south, x), new Point(south, x + 1));

            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }

        boolean westIsEmpty(List<Elv> elves) {
            int west = first.x - 1;
            int y = first.y;

            List<Point> points = List.of(new Point(y - 1, west), new Point(y, west), new Point(y + 1, west));

            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }

        boolean eastIsEmpty(List<Elv> elves) {
            int east = first.x + 1;
            int y = first.y;

            List<Point> points = List.of(new Point(y - 1, east), new Point(y, east), new Point(y + 1, east));

            return elves.stream().map(e -> e.first).noneMatch(points::contains);
        }
    }

    private record Point(int y, int x) {
    }
}
