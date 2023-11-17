package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;

public class Day13 implements Puzzle {
    @Override
    public Integer part1(String input) {
        List<Packet> allPacks = parseAllPacks(input);
        return IntStream.range(1, allPacks.size())
                .filter(i -> i % 2 == 1 && allPacks.get(i).compareTo(allPacks.get(i - 1)) > 0).map(i -> i / 2 + 1).sum();
    }

    @Override
    public Integer part2(String input) {
        List<Packet> allPacks = parseAllPacks(input);
        Packet marker2 = new Packet("[[2]]");
        Packet marker6 = new Packet("[[6]]");
        allPacks.addAll(Arrays.asList(marker2, marker6));
        Collections.sort(allPacks);
        return (allPacks.indexOf(marker2) + 1) * (allPacks.indexOf(marker6) + 1);
    }

    static List<Packet> parseAllPacks(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
        List<Packet> allPacks = new ArrayList<>();
        for (int i = 0; i < strings.size(); i += 3) {
            allPacks.addAll(strings.subList(i, i + 2).stream().map(Packet::new).toList());
        }
        return allPacks;
    }

    abstract static class Entry implements Comparable<Entry> {
    }

    static class Packet extends Entry {
        List<Entry> packets;

        Packet(List<Entry> packets) {
            this.packets = packets;
        }

        static int findClosingBracket(String string, int indexOfStart) {
            int closePos = indexOfStart;
            int counter = 1;
            while (counter > 0) {
                switch (string.charAt(++closePos)) {
                    case '[' -> counter++;
                    case ']' -> counter--;
                    default -> { }
                }
            }
            return closePos;
        }

        Packet(String string) {
            List<Entry> packets = new ArrayList<>();
            String packetStr = string.substring(1, string.length() - 1);
            int position = 0;
            while (position < packetStr.length()) {
                if (packetStr.charAt(position) == '[') {
                    int indexOfEndPack = findClosingBracket(packetStr, position);
                    packets.add(new Packet(packetStr.substring(position, indexOfEndPack) + "]"));
                    position = indexOfEndPack + 2;
                } else {
                    int x = packetStr.indexOf(',', position);
                    packets.add(new Number(Integer.parseInt(x == -1 ? packetStr.substring(position) : packetStr.substring(position, x))));
                    position = x == -1 ? packetStr.length() : x + 1;
                }
            }
            this.packets = packets;
        }

        @Override
        public int compareTo(@NotNull Entry o) {
            return o instanceof Packet ? compareEntries(((Packet) o).packets) : this.compareTo(new Packet(List.of(o)));
        }

        int compareEntries(List<Entry> entries) {
            for (int i = 0; i < Math.min(entries.size(), packets.size()); i++) {
                int result = packets.get(i).compareTo(entries.get(i));
                if (result != 0) return result;
            }
            return Integer.compare(packets.size(), entries.size());
        }
    }

    static class Number extends Entry {
        int number;

        Number(int number) {
            this.number = number;
        }

        @Override
        public int compareTo(@NotNull Entry o) {
            return o instanceof Number ? Integer.compare(number, ((Number) o).number) : -o.compareTo(this);
        }
    }
}
