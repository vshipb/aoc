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
        String[] splitInput = input.split("\n");
        List<Elv> elves = new ArrayList<>();

        boolean north;
        boolean south;
        boolean west;
        boolean east;

        int y;
        int x;

        for (int i = 0; i < splitInput.length; i++) {
            for (int j = 0; j < splitInput[0].length(); j++) {
                if (splitInput[i].charAt(j) == '#') {
                    elves.add(new Elv(i, j));
                }
            }
        }

        int direction;
        int mainDirection = 0;

        for (int i = 0; i < 10; i++) {
            for (Elv elf : elves) {
                north = elf.northIsEmpty(elves);
                south = elf.southIsEmpty(elves);
                west = elf.westIsEmpty(elves);
                east = elf.eastIsEmpty(elves);

                y = elf.first.y;
                x = elf.first.x;

                direction = mainDirection;

                for (int j = 0; j < 4; j++) {
                    if ((north && south && west && east) || (!north && !south && !west && !east)) {
                        elf.second = elf.first;
                        break;
                    }
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
                    if (elf.second != null) break;

                    direction = direction(direction);
                }
            }

            List<Point> duplicates = elves.stream().map(elv -> elv.second)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                    .filter(p -> p.getValue() > 1).map(Map.Entry::getKey).toList();

            for (Elv elv : elves) {
                if (!duplicates.contains(elv.second)) {
                    elv.first = elv.second;
                }
            }

            for (Elv elv : elves) {
                elv.second = null;
            }

            mainDirection = direction(mainDirection);
        }

        x = elves.stream().mapToInt(elv -> elv.first.x).max().orElseThrow()
                - elves.stream().mapToInt(elv -> elv.first.x).min().orElseThrow() + 1;
        y = elves.stream().mapToInt(elv -> elv.first.y).max().orElseThrow()
                - elves.stream().mapToInt(elv -> elv.first.y).min().orElseThrow() + 1;

        return String.valueOf((x * y) - elves.size());
    }

    @Override
    public String part2(String input) {
        String[] splitInput = input.split("\n");
        List<Elv> elves = new ArrayList<>();

        boolean north;
        boolean south;
        boolean west;
        boolean east;

        int y;
        int x;

        for (int i = 0; i < splitInput.length; i++) {
            for (int j = 0; j < splitInput[0].length(); j++) {
                if (splitInput[i].charAt(j) == '#') {
                    elves.add(new Elv(i, j));
                }
            }
        }

        int direction;
        int mainDirection = 0;
        int countNoMove = 0;

        for (int i = 0; i < 1000; i++) {
            for (Elv elf : elves) {
                north = elf.northIsEmpty(elves);
                south = elf.southIsEmpty(elves);
                west = elf.westIsEmpty(elves);
                east = elf.eastIsEmpty(elves);

                y = elf.first.y;
                x = elf.first.x;

                direction = mainDirection;

                for (int j = 0; j < 4; j++) {
                    if ((north && south && west && east) || (!north && !south && !west && !east)) {
                        elf.second = elf.first;
                        countNoMove++;
                        break;
                    }
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
                    if (elf.second != null) break;

                    direction = direction(direction);
                }
            }

            List<Point> duplicates = elves.stream().map(elv -> elv.second)
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
                    .filter(p -> p.getValue() > 1).map(Map.Entry::getKey).toList();

            for (Elv elv : elves) {
                if (!duplicates.contains(elv.second)) {
                    elv.first = elv.second;
                }
            }

            for (Elv elv : elves) {
                elv.second = null;
            }

            mainDirection = direction(mainDirection);

            if (countNoMove == elves.size()) return String.valueOf(i + 1);
            countNoMove = 0;
        }

        return "";
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
            List<Point> points = new ArrayList<>();
            int north = first.y - 1;
            int x = first.x;

            points.add(new Point(north, x - 1));
            points.add(new Point(north, x));
            points.add(new Point(north, x + 1));

            return elves.stream()
                    .map(e -> e.first).filter(points::contains).toList().size() == 0;
        }

        boolean southIsEmpty(List<Elv> elves) {
            List<Point> points = new ArrayList<>();
            int south = first.y + 1;
            int x = first.x;

            points.add(new Point(south, x - 1));
            points.add(new Point(south, x));
            points.add(new Point(south, x + 1));

            return elves.stream()
                    .map(e -> e.first).filter(points::contains).toList().size() == 0;
        }

        boolean westIsEmpty(List<Elv> elves) {
            List<Point> points = new ArrayList<>();
            int west = first.x - 1;
            int y = first.y;

            points.add(new Point(y - 1, west));
            points.add(new Point(y, west));
            points.add(new Point(y + 1, west));

            return elves.stream()
                    .map(e -> e.first).filter(points::contains).toList().size() == 0;
        }

        boolean eastIsEmpty(List<Elv> elves) {
            List<Point> points = new ArrayList<>();
            int east = first.x + 1;
            int y = first.y;

            points.add(new Point(y - 1, east));
            points.add(new Point(y, east));
            points.add(new Point(y + 1, east));

            return elves.stream()
                    .map(e -> e.first).filter(points::contains).toList().size() == 0;
        }
    }

    private record Point(int y, int x) {
    }
}
