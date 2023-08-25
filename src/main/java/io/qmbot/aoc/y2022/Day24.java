package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

public class Day24 implements Puzzle {
    @Override
    public String part1(String input) {
        String[] splitInput = input.split("\n");

        Blizzards[][] field = field(splitInput);

        int maxY = field.length;
        int maxX = field[0].length;

        Blizzards[][][] fieldInTime = fieldInTime(findLCM(maxY - 2, maxX - 2), maxY, maxX, field);

        return String.valueOf(findPath(fieldInTime, new Position(0, 0, 1), maxY - 1, maxX - 2));
    }

    @Override
    public String part2(String input) {
        String[] splitInput = input.split("\n");

        Blizzards[][] field = field(splitInput);

        int maxY = field.length;
        int maxX = field[0].length;

        Blizzards[][][] fieldInTime = fieldInTime(findLCM(maxY - 2, maxX - 2), maxY, maxX, field);

        int a = findPath(fieldInTime, new Position(0, 0, 1), maxY - 1, maxX - 2);
        int b = findPath(fieldInTime, new Position(a, maxY - 1, maxX - 2), 0, 1);
        int c = findPath(fieldInTime, new Position(b, 0, 1), maxY - 1, maxX - 2);

        return String.valueOf(c);
    }

    static int findPath(Blizzards[][][] fieldInTime, Position start, int y, int x) {
        Queue<Position> frontier = new LinkedList<>();
        Set<Position> reached = new HashSet<>();
        reached.add(start);
        frontier.add(start);
        Position current;
        while (!frontier.isEmpty()) {
            current = frontier.poll();
            for (Position next : neighbors(fieldInTime, current)) {
                if (!reached.contains(next)) {
                    frontier.add(next);
                    reached.add(next);
                }
            }
            if (current.y() == y && current.x() == x) return current.t;
        }
        return 0;
    }

    record Position(int t, int y, int x) {
    }

    static List<Position> neighbors(Blizzards[][][] fieldInTime, Position current) {
        List<Position> neighbors = new ArrayList<>();
        int newTime = current.t + 1;
        int y = current.y;
        int x = current.x;
        neighbors.add(new Position(newTime, y, x));
        neighbors.add(new Position(newTime, y + 1, x));
        neighbors.add(new Position(newTime, y - 1, x));
        neighbors.add(new Position(newTime, y, x + 1));
        neighbors.add(new Position(newTime, y, x - 1));
        return neighbors.stream().filter(n -> isPositionValid(n, fieldInTime[newTime % fieldInTime.length])).collect(Collectors.toList());
    }

    static boolean isPositionValid(Position position, Blizzards[][] field) {
        try {
            return (field[position.y][position.x] != null
                    && field[position.y][position.x].blizzards.isEmpty());
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    static Blizzards[][] field(String[] splitInput) {
        Blizzards[][] field = new Blizzards[splitInput.length][splitInput[0].length()];
        for (int y = 0; y < splitInput.length; y++) {
            for (int x = 0; x < splitInput[0].length(); x++) {
                Blizzards add = Blizzards.of(splitInput[y].charAt(x));
                field[y][x] = add;
            }
        }
        return field;
    }

    static Blizzards[][][] fieldInTime(int lcm, int maxY, int maxX, Blizzards[][] field) {
        Blizzards[][][] fieldInTime = new Blizzards[lcm][maxY][maxX];
        for (int i = 0; i < lcm; i++) {
            fieldInTime[i] = field;
            field = move(field);
        }
        return fieldInTime;
    }

    static int findGCD(int a, int b) {
        int c;
        while (b != 0) {
            c = b;
            b = a % b;
            a = c;
        }
        return a;
    }

    static int findLCM(int a, int b) {
        return a * b / findGCD(a, b);
    }

    enum Blizzard {
        UP(-1, 0, "^"), DOWN(1, 0, "v"), LEFT(0, -1, "<"), RIGHT(0, 1, ">");
        final int dy;
        final int dx;
        final String symbol;

        @Override
        public String toString() {
            return symbol;
        }

        Blizzard(int y, int x, String symbol) {
            this.dy = y;
            this.dx = x;
            this.symbol = symbol;
        }


        static Blizzard of(char c) {
            return switch (c) {
                case '^' -> UP;
                case 'v' -> DOWN;
                case '<' -> LEFT;
                case '>' -> RIGHT;
                default -> null;
            };
        }
    }

    static class Blizzards {
        List<Blizzard> blizzards = new ArrayList<>();

        static Blizzards of(char c) {
            Blizzards b = new Blizzards();
            if (c == '.') return b;
            if (Blizzard.of(c) != null) {
                b.blizzards.add(Blizzard.of(c));
                return b;
            }
            return null;
        }

        @Override
        public String toString() {
            if (blizzards.size() == 0) return ".";
            if (blizzards.size() == 1) return blizzards.get(0).toString();
            return String.valueOf(blizzards.size());
        }
    }

    private static Blizzards[][] move(Blizzards[][] field) {
        int lengthY = field.length - 1;
        int lengthX = field[0].length - 1;
        Blizzards[][] fieldAfter = new Blizzards[field.length][field[0].length];
        for (int y = 1; y < lengthY; y++) {
            for (int x = 1; x < lengthX; x++) {
                fieldAfter[y][x] = new Blizzards();
            }
        }
        fieldAfter[0][1] = new Blizzards();
        fieldAfter[lengthY][lengthX - 1] = new Blizzards();
        for (int y = 1; y < lengthY; y++) {
            for (int x = 1; x < lengthX; x++) {
                if (field[y][x] != null) {
                    for (Blizzard blizzard : field[y][x].blizzards) {
                        int yd = (y - 1 + blizzard.dy + (lengthY - 1)) % (lengthY - 1) + 1;
                        int xd = (x - 1 + blizzard.dx + (lengthX - 1)) % (lengthX - 1) + 1;
                        fieldAfter[yd][xd].blizzards.add(blizzard);
                    }
                }
            }
        }
        return fieldAfter;
    }
}
