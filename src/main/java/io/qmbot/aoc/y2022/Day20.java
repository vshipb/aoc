package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day20 implements Puzzle {
    @Override
    public String part1(String input) {
        int[] inputSplit = Stream.of(input.split("\n")).mapToInt(Integer::parseInt).toArray();
        List<AtomicInteger> list = new ArrayList<>();
        List<AtomicInteger> list2 = new ArrayList<>();

        int length = inputSplit.length;

        for (int j : inputSplit) {
            AtomicInteger value = new AtomicInteger(j);
            list.add(value);
            list2.add(value);
        }
        AtomicInteger element;
        int oldIndex;

        for (int i = 0; i < length; i++) {
            element = list2.get(i);
            oldIndex = list.indexOf(element);
            list.remove(oldIndex);
            list.add(newIndex(element.get(), oldIndex, length), element);
        }

        int ex = 0;
        for (int i = 0; i < length; i++) {
            if (list.get(i).get() == 0) {
                ex = i;
            }
        }

        int a = list.get(index(length, 1000 + ex)).get();
        int b = list.get(index(length, 2000 + ex)).get();
        int c = list.get(index(length, 3000 + ex)).get();

        return String.valueOf(a + b + c);
    }

    @Override
    public String part2(String input) {
        return "";
    }

    static int newIndex(int element, int oldIndex, int length) {
        int newIndex;
        newIndex = oldIndex + element;
        if (newIndex >= length) {
            return newIndex % length;
        }
        if (newIndex < 0) {
            newIndex = (newIndex % length);
            newIndex = length + newIndex - 1;
        }
        if (newIndex == 0 && oldIndex != 0) {
            return length - 1;
        }
        return newIndex;
    }

    private static int index(int length, int step) {
        return (length + step) % length;
    }
}
