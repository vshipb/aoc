package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Day05 implements Puzzle {
    @Override
    public String part1(String input) {
        String[] splitted = input.split(REGEX_NEW_LINE + REGEX_NEW_LINE);
        List<LinkedList<Character>> stacksOfCrates = stacksOfCrates(splitted[0].lines().toList());
        crateMover9000(stacksOfCrates, splitted[1].lines().toList(), ((lines, from, to, count) -> {
            for (int i = 0; i < count; i++) {
                lines.get(to).push(lines.get(from).pop());
            }
        }));
        return result(stacksOfCrates);
    }

    @Override
    public String part2(String input) {
        String[] splitted = input.split(REGEX_NEW_LINE + REGEX_NEW_LINE);
        List<LinkedList<Character>> stacksOfCrates = stacksOfCrates(splitted[0].lines().toList());
        crateMover9001(stacksOfCrates, splitted[1].lines().toList(), ((lines, from, to, count) -> {
            LinkedList<Character> reserve = new LinkedList<>();
            for (int i = 0; i < count; i++) {
                reserve.push(lines.get(from).pop());
            }
            for (int i = 0; i < count; i++) {
                lines.get(to).push(reserve.pop());
            }
        }));
        return result(stacksOfCrates);
    }

    private static String result(List<LinkedList<Character>> stacksOfCrates) {
        StringBuilder result = new StringBuilder();
        for (LinkedList<Character> rl : stacksOfCrates) {
            result.append(rl.getFirst());
        }
        return result.toString();
    }

    private static List<LinkedList<Character>> stacksOfCrates(List<String> strings) {
        String stringWithStacks = strings.get(strings.size() - 1).replaceAll(" ", "");
        int countOfStacks = Character.getNumericValue(stringWithStacks.charAt(stringWithStacks.length() - 1));
        List<LinkedList<Character>> stacks = new ArrayList<>();
        for (int i = 0; i < countOfStacks; i++) {
            LinkedList<Character> stack = new LinkedList<>();
            int idx = 1 + (i * 4);
            for (int j = strings.size() - 2; j >= 0; j--) {
                String current = strings.get(j);
                if (idx >= current.length() || current.charAt(idx) == ' ') break;
                stack.push(current.charAt(idx));
            }
            stacks.add(stack);
        }
        return stacks;
    }

    private static void crateMover9000(List<LinkedList<Character>> lines, List<String> moves, Mover mover) {
        for (String string : moves) {
            String[] stringSplit = string.split(" ");
            int count = Integer.parseInt(stringSplit[1]);
            int from = Integer.parseInt(stringSplit[3]) - 1;
            int to = Integer.parseInt(stringSplit[5]) - 1;
            mover.move(lines, from, to, count);
        }
    }

    private static void crateMover9001(List<LinkedList<Character>> lines, List<String> moves, Mover mover) {
        for (String string : moves) {
            String[] stringSplit = string.split(" ");
            int count = Integer.parseInt(stringSplit[1]);
            int from = Integer.parseInt(stringSplit[3]) - 1;
            int to = Integer.parseInt(stringSplit[5]) - 1;
            mover.move(lines, from, to, count);
        }
    }

    interface Mover {
        void move(List<LinkedList<Character>> lines, int from, int to, int count);
    }
}
