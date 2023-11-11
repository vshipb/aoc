package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Day20 implements Puzzle {
    @Override
    public Long part1(String input) {
        return mixing(input, 1, 1);
    }

    @Override
    public Long part2(String input) {
        return mixing(input, 811589153, 10);
    }

    private static Long mixing(String input, int key, int repeat) {
        List<AtomicLong> list = parseInput(input, key);
        List<AtomicLong> initialList = list.stream().toList();
        int length = list.size();
        for (int j = 0; j < repeat; j++) {
            mix(length, list, initialList);
        }
        return answer(list, length, zeroIndex(length, list));
    }

    static long answer(List<AtomicLong> list, int length, int zeroIndex) {
        return list.get(index(length, 1000 + zeroIndex)).get()
                + list.get(index(length, 2000 + zeroIndex)).get()
                + list.get(index(length, 3000 + zeroIndex)).get();
    }

    static int zeroIndex(int length, List<AtomicLong> list) {
        for (int i = 0; i < length; i++) {
            if (list.get(i).get() == 0) {
                return i;
            }
        }
        return 0;
    }

    static int newIndex(long element, int oldIndex, int length) {
        length = length - 1;
        if (length == 0) return 0;
        int newIndex = Math.floorMod(oldIndex + element, length);
        if (newIndex == 0 && oldIndex != 0) {
            return length;
        }
        return newIndex;
    }

    private static void mix(int length, List<AtomicLong> list, List<AtomicLong> list2) {
        for (int i = 0; i < length; i++) {
            AtomicLong element = list2.get(i);
            int oldIndex = list.indexOf(element);
            list.remove(oldIndex);
            list.add(newIndex(element.get(), oldIndex, length), element);
        }
    }

    private static List<AtomicLong> parseInput(String input, long key) {
        int[] inputSplit = Stream.of(input.split("\n")).mapToInt(Integer::parseInt).toArray();
        List<AtomicLong> list = new ArrayList<>();
        for (long j : inputSplit) {
            AtomicLong value = new AtomicLong(j * key);
            list.add(value);
        }
        return list;
    }

    private static int index(int length, int step) {
        return (length + step) % length;
    }
}
