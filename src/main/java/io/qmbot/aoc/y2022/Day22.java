package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;

public class Day22 implements Puzzle {
    @Override
    public String part1(String input) {
        String[] splitInput = input.split("\n\n");
        String[] lines = splitInput[0].split("\n");
        String directions = splitInput[1];

        int maxY = lines.length;
        int maxX = Arrays.stream(lines).map(String::length).max(Integer::compareTo).orElseThrow();

        char[][] boardPoints = new char[maxY][maxX];

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                String line = lines[y];
                boardPoints[y][x] = x < line.length() ? line.charAt(x) : ' ';
            }
        }

        int size = 0;

        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (boardPoints[y][x] != ' ') {
                    size++;
                }
            }
        }

        size = (int) Math.sqrt(size / 6) - 1;
        int numberSquare = 0;

        Square[] squares = new Square[6];
        int boardX;
        int boardY = 0;
        int boardSizeX = 0;
        int boardSizeY = 0;

        for (int y = 0; y < maxY; y++) {
            boardX = 0;
            for (int x = 0; x < maxX; x++) {
                if (boardPoints[y][x] != ' ') {
                    squares[numberSquare] = (new Square(size + 1, new Point(y, x), new Point(y + size, x + size),
                            boardY, boardX));

                    for (int j = 0; j <= size; j++) {
                        for (int i = 0; i <= size; i++) {
                            squares[numberSquare].points[j][i] = boardPoints[y + j][x + i];
                        }
                    }
                    numberSquare++;
                }
                x = x + size;
                boardX++;
                boardSizeX = boardX;
            }
            boardY++;
            boardSizeY = boardY;
            y = y + size;
        }
        Square[][] board = new Square[boardSizeY][boardSizeX];

        for (Square square : squares) {
            board[square.boardY][square.boardX] = square;
        }

        Me me = new Me(0, 0);

        // 0 = R
        // 1 = D
        // 2 = L
        // 3 = U

        for (int x = 0; x < boardSizeX; x++) {
            if (board[0][x] != null) {
                for (int squareX = 0; squareX <= size; squareX++) {
                    if (squares[0].points[0][squareX] == '.') {
                        me.point = new Point(0, squareX);
                        me.boardSquare = new Point(0, x);
                        break;
                    }
                }
                break;
            }
        }

        String number = "";
        boolean isNumber;
        int j;

        for (int i = 0; i < directions.length(); i++) {
            j = i;
            char command = directions.charAt(i);
            if (Character.isDigit(command)) {
                isNumber = true;
                while (isNumber && j < directions.length()) {
                    number += directions.charAt(j);
                    j++;
                    if (j < directions.length()) {
                        isNumber = Character.isDigit(directions.charAt(j));
                    }
                }
                me.move2(Integer.parseInt(number), size + 1, board);
                i = j - 1;
                number = "";
            } else {
                me.round(command);
            }
        }
        int row = (me.boardSquare.pointY * (size + 1)) + me.point.pointY + 1;
        int column = (me.boardSquare.pointX * (size + 1)) + me.point.pointX + 1;
        int facing = me.direction;

        int result = row * 1000 + column * 4 + facing;
        return String.valueOf(result);
    }


    @Override
    public String part2(String input) {
        return "";
    }

    private static class Me {
        private Point point;
        private int direction;
        private Point boardSquare;

        public Me(int y, int x) {
            this.point = new Point(y, x);
            this.direction = 0;
        }

        private void move2(int move, int size, Square[][] board) {
            int boardSizeY = board.length - 1;
            int boardSizeX = board[0].length - 1;
            int boardBeforeX = boardSquare.pointX;
            int boardBeforeY = boardSquare.pointY;
            int newX;
            int newY;
            int x = point.pointX;
            int y = point.pointY;

            switch (direction) {
                case 0 -> {
                    for (int i = 0; i < move; i++) {

                        newX = point.pointX + 1;

                        if (newX == size) {
                            newX = 0;

                            if (boardSquare.pointX == boardSizeX || board[boardSquare.pointY][boardSquare.pointX + 1] == null) {
                                for (int j = 0; j < boardSizeX; j++) {
                                    if (board[boardSquare.pointY][j] != null) {
                                        boardSquare.pointX = j;
                                        break;
                                    }
                                }
                            } else {
                                boardSquare.pointX++;
                            }
                        }

                        if (board[boardSquare.pointY][boardSquare.pointX].points[y][newX] == '.') {
                            point.pointX = newX;
                        } else if (board[boardSquare.pointY][boardSquare.pointX].points[y][newX] == '#') {
                            boardSquare.pointX = boardBeforeX;
                            point.pointX = size - 1;
                        }
                    }
                }
                case 1 -> {
                    for (int i = 0; i < move; i++) {

                        newY = point.pointY + 1;

                        if (newY == size) {
                            newY = 0;

                            if (boardSquare.pointY == boardSizeY || board[boardSquare.pointY + 1][boardSquare.pointX] == null) {
                                for (int j = 0; j < boardSizeY; j++) {
                                    if (board[j][boardSquare.pointX] != null) {
                                        boardSquare.pointY = j;
                                        break;
                                    }
                                }
                            } else {
                                boardSquare.pointY++;
                            }
                        }

                        if (board[boardSquare.pointY][boardSquare.pointX].points[newY][x] == '.') {
                            point.pointY = newY;
                        } else if (board[boardSquare.pointY][boardSquare.pointX].points[newY][x] == '#') {
                            boardSquare.pointY = boardBeforeY;
                            point.pointY = size - 1;
                        }
                    }
                }
                case 2 -> {
                    for (int i = 0; i < move; i++) {

                        newX = point.pointX - 1;

                        if (newX == -1) {
                            newX = size - 1;


                            if (boardSquare.pointX == 0 || board[boardSquare.pointY][boardSquare.pointX - 1] == null) {
                                for (int j = boardSizeX - 1; j > -1; j--) {
                                    if (board[boardSquare.pointY][j] != null) {
                                        boardSquare.pointX = j;
                                        break;
                                    }
                                }
                            } else {
                                boardSquare.pointX--;
                            }
                        }

                        if (board[boardSquare.pointY][boardSquare.pointX].points[y][newX] == '.') {
                            point.pointX = newX;
                        } else if (board[boardSquare.pointY][boardSquare.pointX].points[y][newX] == '#') {
                            boardSquare.pointX = boardBeforeX;
                            point.pointX = 0;
                        }
                    }
                }
                case 3 -> {
                    for (int i = 0; i < move; i++) {
                        newY = point.pointY - 1;

                        if (newY == -1) {
                            newY = size - 1;

                            if (boardSquare.pointY == 0 || board[boardSquare.pointY - 1][boardSquare.pointX] == null) {
                                for (int j = boardSizeY - 1; j > -1; j--) {
                                    if (board[j][boardSquare.pointX] != null) {
                                        boardSquare.pointY = j;
                                        break;
                                    }
                                }
                            } else {
                                boardSquare.pointY--;
                            }
                        }

                        if (board[boardSquare.pointY][boardSquare.pointX].points[newY][x] == '.') {
                            point.pointY = newY;
                        } else if (board[boardSquare.pointY][boardSquare.pointX].points[newY][x] == '#') {
                            boardSquare.pointY = boardBeforeY;
                            point.pointY = 0;
                        }
                    }
                }
                default -> System.out.println("exception");
            }
        }

        private void round(char command) {
            switch (command) {
                case 'R' -> {
                    direction = (direction < 3) ? direction + 1 : 0;
                }
                case 'L' -> {
                    direction = (direction > 0) ? direction - 1 : 3;
                }
                default -> System.out.println("exception");
            }
        }
    }

    private static class Square {
        char[][] points;
        Point first;
        Point second;
        int boardY;
        int boardX;

        public Square(int size, Point first, Point second, int boardY, int boardX) {
            this.points = new char[size][size];
            ;
            this.first = first;
            this.second = second;
            this.boardY = boardY;
            this.boardX = boardX;
        }
    }

    private static class Point {
        int pointY;
        int pointX;

        public Point(int pointY, int pointX) {
            this.pointY = pointY;
            this.pointX = pointX;
        }
    }
}

