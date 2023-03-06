package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;

import java.util.*;


public class Day07 implements Puzzle {
    @Override
    public String part1(String input) {
        Directory directory = new Directory(null, null);
        Directory current = directory;
        Map<String, Directory> dirByName = new HashMap<>();
        for (String string : input.split("\n")) {
            if (string.startsWith("$ cd /")) {
                current = directory;
            } else if (string.startsWith("$ cd ..")) {
                current = current.parent;
            } else if (string.startsWith("$ cd")) {
                current = current.getChild(string.split(" ")[2]);
            } else if (string.startsWith("dir")) {
                Directory dir = new Directory(current, string.split(" ")[1]);
                current.children.add(dir);
                dirByName.put(current.name + dir.name, dir);
            } else if (!string.startsWith("$ ls")) {
                current.children.add(new File(current, Integer.parseInt(string.split(" ")[0]), string.split(" ")[1]));
            }
        }
        long value = 0;
        for (String string : dirByName.keySet()) {
            long s = dirByName.get(string).size();
            if (s < 100000) {
                value = value + s;
            }
        }
        return String.valueOf(value);
    }

    @Override
    public String part2(String input) {
        Directory directory = new Directory(null, null);
        Directory current = directory;
        List<Directory> dirByName = new ArrayList<>();
        for (String string : input.split("\n")) {
            if (string.startsWith("$ cd /")) {
                current = directory;
            } else if (string.startsWith("$ cd ..")) {
                current = current.parent;
            } else if (string.startsWith("$ cd")) {
                current = current.getChild(string.split(" ")[2]);
            } else if (string.startsWith("dir")) {
                Directory dir = new Directory(current, string.split(" ")[1]);
                current.children.add(dir);
                dirByName.add(dir);
            } else if (!string.startsWith("$ ls")) {
                current.children.add(new File(current, Integer.parseInt(string.split(" ")[0]), string.split(" ")[1]));
            }
        }
        long delete = directory.size();
        long maxFull = directory.size() - 40000000;
        for (Directory dir : dirByName) {
            long s = dir.size();
            if (s > maxFull && s < delete) {
                delete = s;
            }
        }
        return String.valueOf(delete);
    }

    abstract static class Entry {
        public Entry(Directory parent, String name) {
            this.parent = parent;
            this.name = name;
        }

        final Directory parent;
        final String name;

        abstract long size();
    }

    static class File extends Entry {
        File(Directory parent, int size, String name) {
            super(parent, name);
            this.size = size;
        }

        int size;

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
            long size = 0;
            for (Entry child : children) {
                size = size + child.size();
            }
            return size;
        }

        Directory getChild(String name) {
            for (Entry child : children) {
                if (Objects.equals(child.name, name)) {
                    return (Directory) child;
                }
            }
            throw new IllegalStateException();
        }
    }
}
