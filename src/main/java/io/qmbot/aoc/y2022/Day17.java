package io.qmbot.aoc.y2022;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day17 {
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\22_17.1.txt";

    public static void main(String[] args) throws IOException {
        String input = Files.readString(Paths.get(PATH));
        System.out.println(part1(input));
        System.out.println(part2(input));
    }

    private static String part1(String input) {
        String[][] field = new String[24276][9];

        for (int j = 0; j < field.length; j++){
            Arrays.fill(field[j], "0");
        }

        for (int j = 0; j < field.length; j++) {
            field[j][0] = "2";
            field[j][8] = "2";
        }
        for (int j = 0; j < field[0].length; j++) {
            field[field.length - 1][j] = "2";
        }

        int startDirection = 0;
        int start = field.length - 5;
        Figure figure = null;
        int indexF = 0;

        for (int NumberF = 0; NumberF < 2022; NumberF++) {
            for (String[] string : field) {
                if (Arrays.asList(string).contains("7")) {
                    start = Arrays.asList(field).indexOf(string) - 4;
                    break;
                }
            }

            switch (indexF) {
                case 0 -> figure = horizontal(start);
                case 1 -> figure = plus(start);
                case 2 -> figure = angle(start);
                case 3 -> figure = vertical(start);
                case 4 -> figure = square(start);
            }

            startDirection = figure.startFalling(field, input, startDirection);

            if (indexF == 4) indexF = -1;
            indexF++;
        }
        int high = 0;
        for (String[] string : field){
            if (Arrays.asList(string).contains("7")) {
                high = Arrays.asList(field).indexOf(string);
                break;
            }
        }

        high = field.length - high - 1;
        return String.valueOf(high);
    }

    private static String part2(String input) {
        int highField = 150;
        String[][] field = new String[highField][9];
        long g = 0;

        for (int j = 0; j < field.length; j++){
            Arrays.fill(field[j], "0");
        }

        for (int j = 0; j < field.length; j++) {
            field[j][0] = "2";
            field[j][8] = "2";
        }
        for (int j = 0; j < field[0].length; j++) {
            field[field.length - 1][j] = "2";
        }

        int startDirection = 0;
        int[] start = new int[1];
        start[0] = field.length - 5;
        Figure figure = null;
        int indexF = 0;

        for (long NumberF = 0; NumberF < 1_000_000_000L; NumberF++) {

            for (int i = 0; i < field.length; i++) {
                if (Arrays.asList(field[i]).contains("7")) {

                    if (i == 30 || i == 31 || i == 32){
                        g = g + deleteBlocks(i, field, highField, start);
                        break;
                    }

                    start[0] = Arrays.asList(field).indexOf(field[i]) - 4;
                    break;
                }
            }

            switch (indexF) {
                case 0 -> figure = horizontal(start[0]);
                case 1 -> figure = plus(start[0]);
                case 2 -> figure = angle(start[0]);
                case 3 -> figure = vertical(start[0]);
                case 4 -> figure = square(start[0]);
            }

            startDirection = figure.startFalling(field, input, startDirection);

            if (indexF == 4) indexF = -1;
            indexF++;
        }

        int high = 0;
        for (String[] string : field){
            if (Arrays.asList(string).contains("7")) {
                high = Arrays.asList(field).indexOf(string);
                break;
            }
        }

        g = g + (field.length - high - 1);
        return String.valueOf(g);

    }

    static long deleteBlocks(int i, String[][] field, int highField, int[] start){
        int max = highField - 2;
        int min = max - i;
        int change = i;
        for (int y = min; y < max; y++){
            for (int x = 1; x < 8; x++){
                field[y][x] = field[change][x];
            }
            change++;
        }
        for (int y = 0; y < min; y++){
            for(int x = 1; x < 8; x++){
                field[y][x] = "0";
            }
        }
        long move = min - i;
        start[0] = min - 4;
        return move;
    }

    static class Point {
        int x;
        int y;

        public Point(int y, int x) {
            this.y = y;
            this.x = x;
        }
    }

    static class Figure {

        List<Point> figurePoints = new ArrayList<>();

        public Figure(Point... points) {
            for (Point p : points) {
                figurePoints.add(p);
            }
        }


        int startFalling(String[][] field, String direction, int startDirection) {
            boolean spaceForFalling = true;
            while (spaceForFalling) {
                boolean space = true;

                for (Point point : figurePoints) {
                    field[point.y][point.x] = "0";
                }
                int maxX = 1;
                int minX = 7;
                int maxY = 0;

                for (Point point : figurePoints) {
                    minX = Math.min(minX, point.x); //не двигать в право
                    maxX = Math.max(maxX, point.x);
                    maxY = Math.max(maxY, point.y);
                }

                switch (direction.charAt(startDirection)) {
                    case '<' -> {
                        for (Point point : figurePoints) {
                            if (field[point.y][point.x - 1] != "0") {
                                space = false;
                            }
                        }
                        if (space) {
                            for (Point point : figurePoints) {
                                point.x = point.x - 1;
                            }
                        }
                    }

                    case '>' -> {
                        for (Point point : figurePoints) {
                            if (field[point.y][point.x + 1] != "0") {
                                space = false;
                            }
                        }
                        if (space) {
                            for (Point point : figurePoints) {
                                point.x = point.x + 1;
                            }
                        }
                    }
                }
                startDirection++;
                if (startDirection == direction.length()) startDirection = 0;

                for (Point point : figurePoints) {
                    if (field[point.y + 1][point.x] != "0") {
                        spaceForFalling = false;
                        break;
                    }
                }

                if (spaceForFalling) {
                    for (Point point : figurePoints) {
                        point.y = point.y + 1;
                    }
                }
                for (Point point : figurePoints) {
                    field[point.y][point.x] = "7";
                }
            }
            return startDirection;
        }


    }

    static Figure horizontal(int startY) {
        return new Figure(new Point(startY, 3), new Point(startY, 4), new Point(startY, 5), new Point(startY, 6));
    }

    static Figure plus(int startY) {
        return new Figure(new Point(startY, 4), new Point(startY - 1, 4), new Point(startY - 2, 4), new Point(startY - 1, 3), new Point(startY - 1, 5));
    }

    static Figure angle(int startY) {
        return new Figure(new Point(startY, 5), new Point(startY - 1, 5), new Point(startY - 2, 5), new Point(startY, 4), new Point(startY, 3));
    }

    static Figure vertical(int startY) {
        return new Figure(new Point(startY, 3), new Point(startY - 1, 3), new Point(startY - 2, 3), new Point(startY - 3, 3));
    }

    static Figure square(int startY) {
        return new Figure(new Point(startY, 3), new Point(startY, 4), new Point(startY - 1, 3), new Point(startY - 1, 4));
    }
}

