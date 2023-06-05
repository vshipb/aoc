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
            board[square.squareY][square.squareX] = square;
        }
        Me me = new Me(0, 0);
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
        int facing = me.direction.ordinal();

        int result = row * 1000 + column * 4 + facing;
        return String.valueOf(result);
    }

    @Override
    public String part2(String input) {
        return null;
    }


    enum Direction {
        East(0, 1), South(1, 0), West(0, -1), North(-1, 0);
        final int dy;
        final int dx;

        Direction(int dy, int dx) {
            this.dy = dy;
            this.dx = dx;
        }

        public Point getPoint(Point p) {
            return new Point(p.pointY + dy, p.pointX + dx);
        }

        public Point getPoint(Point p, int sizeY, int sizeX) {
            return new Point((p.pointY + dy + sizeY) % sizeY, (p.pointX + dx + sizeX) % sizeX);
        }

        public Point getPoint(Point p, Square[][] board) {
            while (true) {
                p = getPoint(p, board.length, board[0].length);
                if (board[p.pointY][p.pointX] != null) {
                    return p;
                }
            }
        }

    }

    private static class Me {
        private Point point;
        private Direction direction;
        private Point boardSquare;

        public Me(int y, int x) {
            this.point = new Point(y, x);
            this.direction = Direction.East;
        }

        private void move2(int move, int size, Square[][] board) {
            for (int i = 0; i < move; i++) {
                Point boardBefore = boardSquare;
                Point next = direction.getPoint(point);
                if (next.pointX == size || next.pointX == -1 || next.pointY == size || next.pointY == -1) {
                    next = direction.getPoint(point, size, size);
                    boardSquare = direction.getPoint(boardSquare, board);
                }

                switch (board[boardSquare.pointY][boardSquare.pointX].points[next.pointY][next.pointX]) {
                    case '.' -> point = next;
                    case '#' -> {
                        boardSquare = boardBefore;
                    }
                    default -> System.out.println("exception");
                }
            }
        }

        private void round(char command) {
            direction = Direction.values()[(direction.ordinal() + (command == 'R' ? 1 : 3)) % 4];
        }
    }

    private static class Square {
        char[][] points;
        Point first;
        Point second;
        int squareY;
        int squareX;

        public Square(int size, Point first, Point second, int squareY, int squareX) {
            this.points = new char[size][size];
            ;
            this.first = first;
            this.second = second;
            this.squareY = squareY;
            this.squareX = squareX;
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

