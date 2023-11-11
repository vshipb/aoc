package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day07 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return dir(input).allDirs().stream().mapToInt(Directory::size).filter(s -> s < 100000).sum();
    }

    @Override
    public Integer part2(String input) {
        Directory root = dir(input);
        return root.allDirs().stream().filter(d -> d != root).mapToInt(Directory::size)
                .filter(s -> s > root.size() - 40000000).min().orElse(root.size());
    }

    private static Directory dir(String input) {
        Directory directory = new Directory(null);
        Directory current = directory;
        ArrayDeque<Directory> stack = new ArrayDeque<>();
        for (String string : input.split(REGEX_NEW_LINE)) {
            String[] split = string.split(" ");
            switch (split[0]) {
                case "$" -> {
                    if ("cd".equals(split[1])) {
                        switch (split[2]) {
                            case "/" -> current = directory;
                            case ".." -> current = stack.pop();
                            default -> {
                                stack.push(current);
                                current = current.getChild(split[2]);
                            }
                        }
                    }
                }
                case "dir" -> current.children.add(new Directory(split[1]));
                default -> current.children.add(new File(split[1], Integer.parseInt(split[0])));
            }
        }
        return directory;
    }

    abstract static class Entry {
        final String name;

        public Entry(String name) {
            this.name = name;
        }

        abstract int size();
    }

    static class File extends Entry {
        int size;

        File(String name, int size) {
            super(name);
            this.size = size;
        }

        @Override
        int size() {
            return size;
        }
    }

    static class Directory extends Entry {
        List<Entry> children = new ArrayList<>();

        Directory(String name) {
            super(name);
        }

        @Override
        int size() {
            return children.stream().mapToInt(Entry::size).sum();
        }

        List<Directory> allDirs() {
            return Stream.concat(Stream.of(this), children.stream().filter(c -> c instanceof Directory)
                    .flatMap(c -> ((Directory) c).allDirs().stream())).toList();
        }

        Directory getChild(String name) {
            return (Directory) children.stream().filter(c -> c.name.equals(name)).findAny().orElseThrow();
        }
    }
}
