package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Day07 implements Puzzle {
    @Override
    public String part1(String input) {
        return String.valueOf(dir(input).allDirs().stream().mapToLong(Directory::size).filter(s -> s < 100000).sum());
    }

    @Override
    public String part2(String input) {
        Directory root = dir(input);
        return String.valueOf(root.allDirs().stream().filter(d -> d != root).mapToLong(Directory::size)
                .filter(s -> s > root.size() - 40000000).min().orElse(root.size()));
    }

    private static Directory dir(String input) {
        Directory directory = new Directory(null, null);
        Directory current = directory;
        for (String string : input.split("\n")) {
            String[] split = string.split(" ");
            switch (split[0]) {
                case "$" -> {
                    if ("cd".equals(split[1])) {
                        switch (split[2]) {
                            case "/" -> current = directory;
                            case ".." -> current = current.parent;
                            default -> current = current.getChild(split[2]);
                        }
                    }
                }
                case "dir" -> current.children.add(new Directory(current, split[1]));
                default -> current.children.add(new File(current, Integer.parseInt(split[0]), split[1]));
            }
        }
        return directory;
    }

    abstract static class Entry {
        final Directory parent;
        final String name;

        public Entry(Directory parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        abstract long size();
    }

    static class File extends Entry {
        int size;

        File(Directory parent, int size, String name) {
            super(parent, name);
            this.size = size;
        }

        @Override
        long size() {
            return size;
        }
    }

    static class Directory extends Entry {
        List<Entry> children = new ArrayList<>();

        Directory(Directory parent, String name) {
            super(parent, name);
        }

        @Override
        long size() {
            return children.stream().mapToLong(Entry::size).sum();
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
