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
        char[][] boardPoints = boardPoints(maxY, maxX, lines);
        int size = size(maxY, maxX, boardPoints);
        int boardSizeX;
        int boardSizeY;
        Square[] squares = squares(maxY, maxX, size, boardPoints);
        if (maxX > maxY) {
            boardSizeX = 4;
            boardSizeY = 3;
        } else {
            boardSizeX = 3;
            boardSizeY = 4;
        }
        Square[][] board = new Square[boardSizeY][boardSizeX];
        for (Square square : squares) {
            board[square.squareY][square.squareX] = square;
        }
        Me me = me(boardSizeX, size, board, squares);
        movingPart1(directions, me, size, 1);
        return result(me.boardSquare.pointY, me.boardSquare.pointX, size, me);
    }

    @Override
    public String part2(String input) {
        String[] splitInput = input.split("\n\n");
        String[] lines = splitInput[0].split("\n");
        String directions = splitInput[1];
        int maxY = lines.length;
        int maxX = Arrays.stream(lines).map(String::length).max(Integer::compareTo).orElseThrow();
        char[][] boardPoints = boardPoints(maxY, maxX, lines);
        int size = size(maxY, maxX, boardPoints);
        int boardSizeX;
        int boardSizeY;
        Square[] squares = squares(maxY, maxX, size, boardPoints);
        if (maxX > maxY) {
            boardSizeX = 4;
            boardSizeY = 3;
        } else {
            boardSizeX = 3;
            boardSizeY = 4;
        }
        Square[][] board = new Square[boardSizeY][boardSizeX];
        for (Square square : squares) {
            board[square.squareY][square.squareX] = square;
        }
        changeMap(squares, boardSizeX);
        Me me = me(boardSizeX, size, board, squares);
        movingPart1(directions, me, size, 2);
        return result(me.square.coordinateOnBoard.pointY, me.square.coordinateOnBoard.pointX, size, me);
    }

    void movingPart1(String directions, Me me, int size, int part) {
        StringBuilder number = new StringBuilder();
        boolean isNumber;
        int j;
        for (int i = 0; i < directions.length(); i++) {
            j = i;
            char command = directions.charAt(i);
            if (Character.isDigit(command)) {
                isNumber = true;
                while (isNumber && j < directions.length()) {
                    number.append(directions.charAt(j));
                    j++;
                    if (j < directions.length()) {
                        isNumber = Character.isDigit(directions.charAt(j));
                    }
                }
                if (part == 1) {
                    me.move1(Integer.parseInt(number.toString()), size + 1);
                } else {
                    me.move2(Integer.parseInt(number.toString()), size + 1);
                }
                i = j - 1;
                number = new StringBuilder();
            } else {
                me.round(command);
            }
        }
    }

    String result(int pointY, int pointX, int size, Me me) {
        int row = pointY * (size + 1) + me.point.pointY + 1;
        int column = pointX * (size + 1) + me.point.pointX + 1;
        int facing = me.direction.ordinal();

        return String.valueOf(row * 1000 + column * 4 + facing);
    }

    Me me(int boardSizeX, int size, Square[][] board, Square[] squares) {
        Me me = new Me(0, 0, squares[0], board);
        for (int x = 0; x < boardSizeX; x++) {
            if (board[0][x] != null) {
                for (int squareX = 0; squareX <= size; squareX++) {
                    if (squares[0].points[0][squareX] == '.') {
                        me.point = new Point(0, squareX);
                        me.boardSquare = new Point(0, x);
                        return me;
                    }
                }
            }
        }
        return me;
    }

    int size(int maxY, int maxX, char[][] boardPoints) {
        int size = 0;
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                if (boardPoints[y][x] != ' ') {
                    size++;
                }
            }
        }
        return (int) Math.sqrt(size / 6) - 1;
    }

    char[][] boardPoints(int maxY, int maxX, String[] lines) {
        char[][] boardPoints = new char[maxY][maxX];
        for (int y = 0; y < maxY; y++) {
            for (int x = 0; x < maxX; x++) {
                String line = lines[y];
                boardPoints[y][x] = x < line.length() ? line.charAt(x) : ' ';
            }
        }
        return boardPoints;
    }

    Square[] squares(int maxY, int maxX, int size, char[][] boardPoints) {
        Square[] squares = new Square[6];
        int numberSquare = 0;
        int boardY = 0;
        for (int y = 0; y < maxY; y++) {
            int boardX = 0;
            for (int x = 0; x < maxX; x++) {
                if (boardPoints[y][x] != ' ') {
                    squares[numberSquare] = (new Square(size + 1, new Point(y, x), new Point(y + size, x + size),
                            boardY, boardX));

                    for (int j = 0; j <= size; j++) {
                        if (size + 1 >= 0)
                            System.arraycopy(boardPoints[y + j], x, squares[numberSquare].points[j], 0, size + 1);
                    }
                    numberSquare++;
                }
                x = x + size;
                boardX++;
            }
            boardY++;
            y = y + size;
        }
        return squares;
    }

    void changeMap(Square[] squares, int boardX) {
        int[][] connections;
        switch (boardX) {
            case 3 ->
                    connections = new int[][]{{5, 1, 2, 3}, {5, 4, 2, 0}, {0, 1, 4, 3}, {2, 4, 5, 0}, {2, 1, 5, 3}, {3, 4, 1, 0}};
            case 4 ->
                    connections = new int[][]{{1, 5, 3, 2}, {2, 3, 4, 5}, {0, 2, 4, 1}, {5, 0, 4, 2}, {3, 5, 1, 2}, {3, 0, 1, 4}};
            default -> {
                System.out.println("Wrong board");
                return;
            }
        }

        for (int i = 0; i < 6; i++) {
            squares[i].top = squares[connections[i][0]];
            squares[i].right = squares[connections[i][1]];
            squares[i].bottom = squares[connections[i][2]];
            squares[i].left = squares[connections[i][3]];
        }
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
        private Square[][] board;

        public Me(int y, int x, Square square, Square[][] board) {
            this.point = new Point(y, x);
            this.direction = Direction.East;
            this.square = square;
            this.board = board;
        }

        private void move1(int move, int size) {
            for (int i = 0; i < move; i++) {
                Point boardBefore = boardSquare;
                Point next = direction.getPoint(point);
                if (isOutOfBounds(next, size)) {
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


        private void move2(int move, int size) {
            for (int i = 0; i < move; i++) {
                Square squareBefore = square;
                Point boardBefore = boardSquare;
                Direction directionBefore = direction;

                Point next = direction.getPoint(point);
                if (isOutOfBounds(next, size)) {
                    square = getNeighborSquare(square, direction);
                    round2(squareBefore, square);
                    next = direction.getPoint(point, size - 1, directionBefore);
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

        private boolean isOutOfBounds(Point point, int size) {
            return point.pointX == size || point.pointX == -1 || point.pointY == size || point.pointY == -1;
        }

        private Square getNeighborSquare(Square square, Direction direction) {
            switch (direction) {
                case North -> {
                    return square.top;
                }
                case East -> {
                    return square.right;
                }
                case South -> {
                    return square.bottom;
                }
                case West -> {
                    return square.left;
                }
                default -> {
                    System.out.println("wrong direction");
                    return square;
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

