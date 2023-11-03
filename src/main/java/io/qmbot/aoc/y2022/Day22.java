package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;

public class Day22 implements Puzzle {
    @Override
    public Integer part1(String input) {
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
        return result;
    }
  
    @Override
    public String part2(String input) {
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
        int boardX = 0;
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

        switch (boardX) {
            case 3 -> {
                squares[0].top = squares[5];
                squares[0].right = squares[1];
                squares[0].bottom = squares[2];
                squares[0].left = squares[3];

                squares[1].top = squares[5];
                squares[1].right = squares[4];
                squares[1].bottom = squares[2];
                squares[1].left = squares[0];

                squares[2].top = squares[0];
                squares[2].right = squares[1];
                squares[2].bottom = squares[4];
                squares[2].left = squares[3];

                squares[3].top = squares[2];
                squares[3].right = squares[4];
                squares[3].bottom = squares[5];
                squares[3].left = squares[0];

                squares[4].top = squares[2];
                squares[4].right = squares[1];
                squares[4].bottom = squares[5];
                squares[4].left = squares[3];

                squares[5].top = squares[3];
                squares[5].right = squares[4];
                squares[5].bottom = squares[1];
                squares[5].left = squares[0];
            }
            case 4 -> {
                squares[0].top = squares[1];
                squares[0].right = squares[5];
                squares[0].bottom = squares[3];
                squares[0].left = squares[2];

                squares[1].top = squares[0];
                squares[1].right = squares[2];
                squares[1].bottom = squares[4];
                squares[1].left = squares[5];

                squares[2].top = squares[0];
                squares[2].right = squares[3];
                squares[2].bottom = squares[4];
                squares[2].left = squares[1];

                squares[3].top = squares[0];
                squares[3].right = squares[5];
                squares[3].bottom = squares[4];
                squares[3].left = squares[2];

                squares[4].top = squares[3];
                squares[4].right = squares[5];
                squares[4].bottom = squares[1];
                squares[4].left = squares[2];

                squares[5].top = squares[3];
                squares[5].right = squares[0];
                squares[5].bottom = squares[1];
                squares[5].left = squares[4];
            }
            default -> System.out.println("Wrong board");
        }
        Me me = new Me(0, 0, squares[0]);
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
                me.move(Integer.parseInt(number), size + 1);
                i = j - 1;
                number = "";
            } else {
                me.round(command);
            }
        }

        int row = (me.square.coordinateOnBoard.pointY * (size + 1)) + me.point.pointY + 1;
        int column = (me.square.coordinateOnBoard.pointX * (size + 1)) + me.point.pointX + 1;
        int facing = me.direction.ordinal();

        int result = row * 1000 + column * 4 + facing;
        return result;
    }

    enum Direction {
        East(0, 1) {
            public Point getPoint(Point p, int size, Direction directionBefore) {
                switch (directionBefore) {
                    case East -> {
                        return new Point(p.pointY, 0);
                    }
                    case South -> {
                        return new Point(size - p.pointX, 0);
                    }
                    case West -> {
                        return new Point(size - p.pointY, 0);
                    }
                    case North -> {
                        return new Point(p.pointX, 0);
                    }
                    default -> System.out.println("wrong direction");
                }
                return p;
            }
        },
        South(1, 0) {
            public Point getPoint(Point p, int size, Direction directionBefore) {
                switch (directionBefore) {
                    case East -> {
                        return new Point(0, size - p.pointY);
                    }
                    case South -> {
                        return new Point(0, p.pointX);
                    }
                    case West -> {
                        return new Point(0, p.pointY);
                    }
                    case North -> {
                        return new Point(0, size - p.pointX);
                    }
                    default -> System.out.println("wrong direction");
                }
                return p;
            }
        },
        West(0, -1) {
            public Point getPoint(Point p, int size, Direction directionBefore) {
                switch (directionBefore) {
                    case East -> {
                        return new Point(size - p.pointY, size);
                    }
                    case South -> {
                        return new Point(p.pointX, size);
                    }
                    case West -> {
                        return new Point(p.pointY, size);
                    }
                    case North -> {
                        return new Point(size - p.pointX, size);
                    }
                    default -> System.out.println("wrong direction");
                }
                return p;
            }
        },

        North(-1, 0) {
            public Point getPoint(Point p, int size, Direction directionBefore) {
                switch (directionBefore) {
                    case East -> {
                        return new Point(size, p.pointY);
                    }
                    case South -> {
                        return new Point(size, size - p.pointX);
                    }
                    case West -> {
                        return new Point(size, size - p.pointY);
                    }
                    case North -> {
                        return new Point(size, p.pointX);
                    }
                    default -> System.out.println("wrong direction");
                }
                return p;
            }
        };
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

        public Point getPoint(Point p, int size, Direction directionBefore) {
            return null;
        }
    }

    private static class Me {
        private Point point;
        private Direction direction;
        private Point boardSquare;
        private Square square;

        public Me(int y, int x) {
            this.point = new Point(y, x);
            this.direction = Direction.East;
        }

        public Me(int y, int x, Square square) {
            this.point = new Point(y, x);
            this.direction = Direction.East;
            this.square = square;
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

        private void move(int move, int size) {
            for (int i = 0; i < move; i++) {
                Square squareBefore = square;
                Point boardBefore = boardSquare;
                Direction directionBefore = direction;
                Point next = direction.getPoint(point);
                if (next.pointX == size || next.pointX == -1 || next.pointY == size || next.pointY == -1) {
                    switch (direction) {
                        case North -> square = square.top;
                        case East -> square = square.right;
                        case South -> square = square.bottom;
                        case West -> square = square.left;
                        default -> System.out.println("wrong direction");
                    }
                    round2(squareBefore, square);
                    next = direction.getPoint(point, size - 1, directionBefore);
                    int b = 0;
                    // boardSquare = square.;
                }

                switch (square.points[next.pointY][next.pointX]) {
                    case '.' -> point = next;
                    case '#' -> {
                        square = squareBefore;
                        direction = directionBefore;
                        boardSquare = boardBefore;
                    }
                    default -> System.out.println("exception");
                }
            }
        }

        private void round(char command) {
            direction = Direction.values()[(direction.ordinal() + (command == 'R' ? 1 : 3)) % 4];
        }

        private void round2(Square squareBefore, Square square) {
            if (squareBefore == square.top) {
                direction = Direction.South;
            }
            if (squareBefore == square.right) {
                direction = Direction.West;
            }
            if (squareBefore == square.bottom) {
                direction = Direction.North;
            }
            if (squareBefore == square.left) {
                direction = Direction.East;
            }
        }
    }

    private static class Square {
        char[][] points;
        Point first;
        Point second;

        Point coordinateOnBoard;
        int squareY;
        int squareX;
        Square top;
        Square right;
        Square bottom;
        Square left;


        public Square(int size, Point first, Point second, int squareY, int squareX) {
            this.points = new char[size][size];
            this.first = first;
            this.second = second;
            this.coordinateOnBoard = new Point(squareY, squareX);
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

