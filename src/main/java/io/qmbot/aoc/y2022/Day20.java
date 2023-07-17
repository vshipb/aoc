package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Day20 implements Puzzle {
    @Override
    public String part1(String input) {
        return  mixing(input, 1, 1);
    }

    @Override
    public String part2(String input) {
        return mixing(input, 811589153, 10);
    }

    private static String mixing(String input, int key, int repeat) {
        int[] inputSplit = Stream.of(input.split("\n")).mapToInt(Integer::parseInt).toArray();
        List<AtomicLong> list = new ArrayList<>();
        List<AtomicLong> list2 = new ArrayList<>();
        int length = inputSplit.length;
        fillSheets(inputSplit, list, list2, key);
        for (int j = 0; j < repeat; j++) {
            mix(length, list, list2);
        }
        return String.valueOf(answer(list, length, nullIndex(length, list)));
    }
    static long answer(List<AtomicLong> list, int length, int ex) {
        return list.get(index(length, 1000 + ex)).get()
                + list.get(index(length, 2000 + ex)).get()
                +list.get(index(length, 3000 + ex)).get();
    }

    static int nullIndex(int length, List<AtomicLong> list) {
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
        int newIndex;
        newIndex = (int) ((oldIndex + element) % length);
        if (newIndex < 0) {
            return length + newIndex;
        }
        if (newIndex == 0 && oldIndex != 0) {
            return length;
        }
        return newIndex;
    }
    private static void mix(int length, List<AtomicLong>  list, List<AtomicLong>  list2) {
        for (int i = 0; i < length; i++) {
            AtomicLong element = list2.get(i);
            int oldIndex = list.indexOf(element);
            list.remove(oldIndex);
            list.add(newIndex(element.get(), oldIndex, length), element);
        }
    }
     private static void fillSheets(int[] inputSplit, List<AtomicLong>  list, List<AtomicLong>  list2, long key) {
        for (long j : inputSplit) {
            AtomicLong value = new AtomicLong(j * key);
            list.add(value);
            list2.add(value);
        }
    }

    private static int index(int length, int step) {
        return (length + step) % length;
    }
}
