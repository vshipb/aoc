package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class Day20 implements Puzzle {
    @Override
    public String part1(String input) {
        Map<AtomicInteger, Integer> map2 = new LinkedHashMap<>();
        int[] inputSplit = Stream.of(input.split("\n")).mapToInt(Integer::parseInt).toArray();
        List<AtomicInteger> list = new ArrayList<>();
        List<AtomicInteger> list2 = new ArrayList<>();

        int length = inputSplit.length;

        for (int i = 0; i < length; i++) {
            AtomicInteger value = new AtomicInteger(inputSplit[i]);
            map2.put(value, i);
            list.add(value);
            list2.add(value);
        }
        AtomicInteger element;
        int oldIndex;

        for (int i = 0; i < length; i++) {
            element = list2.get(i);
            oldIndex = list.indexOf(element);
            list.remove(oldIndex);
            list.add(newIndex(element, oldIndex, length), element);
            map2.clear();

            for (int j = 0; j < length; j++) {
                map2.put(list.get(j), j);
            }
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

    private static int newIndex(AtomicInteger element, int oldIndex, int length) {
        int newIndex;
        newIndex = oldIndex + element.get();
        if (newIndex > length) {
            return (newIndex % length) + 1;
        } else if (newIndex < 0) {
            return length + (newIndex % length) - 1;
        } else if (newIndex == 0) {
            return length - 1;
        }
        return newIndex;
    }

    private static int index(int length, int step) {
        return (length + step) % length;
    }
}
