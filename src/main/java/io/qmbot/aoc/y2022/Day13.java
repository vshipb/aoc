package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day13 implements Puzzle {
    @Override
    public Integer part1(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
        Packet packetLeft;
        Packet packetRight;
        int trueResult = 0;
        int numberOfPair = 0;
        for (int i = 0; i < strings.size(); i = i + 3) {
            numberOfPair++;
            packetLeft = new Packet(strings.get(i));
            packetRight = new Packet(strings.get(i + 1));
            if (packetRight.compareTo(packetLeft) > 0) {
                trueResult = trueResult + numberOfPair;
            }
        }
        return trueResult;
    }

    @Override
    public Integer part2(String input) {
        List<String> strings = List.of(input.split(REGEX_NEW_LINE));
        List<Packet> allPacks = new ArrayList<>();
        for (int i = 0; i < strings.size(); i = i + 3) {
            allPacks.add(new Packet(strings.get(i)));
            allPacks.add(new Packet(strings.get(i + 1)));
        }
        Packet marker2 = new Packet("[[2]]");
        Packet marker6 = new Packet("[[6]]");
        int index2 = 0;
        int index6 = 0;
        allPacks.add(marker2);
        allPacks.add(marker6);
        Collections.sort(allPacks);
        for (int i = 0; i < allPacks.size(); i++) {
            if (allPacks.get(i).equals(marker2)) {
                index2 = i + 1;
            } else if (allPacks.get(i).equals(marker6)) {
                index6 = i + 1;
            }
        }
        return index2 * index6;
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
                char c = string.charAt(++closePos);
                if (c == '[') {
                    counter++;
                } else if (c == ']') {
                    counter--;
                }
            }
            return closePos;
        }

        Packet(String string) {
            List<Entry> packets = new ArrayList<>();
            int size = string.length();
            String packetStr = string.substring(1, size - 1);
            int position = 0;
            while (position < packetStr.length()) {
                if (packetStr.charAt(position) == '[') {
                    int indexOfEndPack = findClosingBracket(packetStr, position);
                    packets.add(new Packet(packetStr.substring(position, indexOfEndPack) + "]"));
                    position = indexOfEndPack + 2;
                } else {
                    int x = packetStr.indexOf(',', position);
                    if (x == -1) {
                        packets.add(new Number(Integer.parseInt(packetStr.substring(position))));
                        break;
                    } else {
                        packets.add(new Number(Integer.parseInt(packetStr.substring(position, x))));
                    }
                    position = x + 1;
                }
            }
            this.packets = packets;
        }

        @Override
        public int compareTo(Entry o) {
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
        public int compareTo(Entry o) {
            return o instanceof Number ? Integer.compare(number, ((Number) o).number) : -o.compareTo(this);
        }
    }
}
