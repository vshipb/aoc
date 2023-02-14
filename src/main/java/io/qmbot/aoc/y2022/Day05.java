package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Day05 implements Puzzle {
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\22_5.txt";

    public static void main(String[] args) throws IOException {
        Puzzle puzzle = new Day05();
        String input = Files.readString(Paths.get(PATH));
        System.out.println(puzzle.part1(input));
        System.out.println(puzzle.part2(input));
    }

    @Override
    public String part1(String input) {
        String[] splitted = input.split("\n\n");
        List<String> strings = splitted[0].lines().toList();
        String str = strings.get(strings.size()-1);
        int stacks = Character.getNumericValue(str.charAt(str.length() - 2));
        LinkedList<Character>[] l = new LinkedList[stacks];
        for (int i = 0; i < stacks; i++) {
            l[i] = new LinkedList<>();
            int a = 1 + (i * 4);
            for (int j = strings.size() - 2; j > -1; j--) {
                String string = strings.get(j);
                if (string.length() < a || string.charAt(a) == ' ') {
                    break;
                }
                l[i].push(string.charAt(a));
            }
        }
        List<String> moves = splitted[1].lines().toList();
        for (String move : moves){
            moving(l, move);
        }
        String result = "";
        for (LinkedList<Character> rl : l) {
            result = result + rl.getFirst();
        }
        return result;
    }

    @Override
    public String part2(String input) {
        String[] splitted = input.split("\n\n");
        List<String> strings = splitted[0].lines().toList();
        String str = strings.get(strings.size()-1);
        int stacks = Character.getNumericValue(str.charAt(str.length() - 2));
        LinkedList<Character>[] l = new LinkedList[stacks];
        for (int i = 0; i < stacks; i++) {
            l[i] = new LinkedList<>();
            int a = 1 + (i * 4);
            for (int j = strings.size() - 2; j > -1; j--) {
                String string = strings.get(j);
                if (string.length() < a || string.charAt(a) == ' ') {
                    break;
                }
                l[i].push(string.charAt(a));
            }
        }
        List<String> moves = splitted[1].lines().toList();
        for (String move : moves){
            moving9001(l, move);
        }
        String result = "";
        for (LinkedList<Character> rl : l) {
            result = result + rl.getFirst();
        }
        return result;
    }

    private static void moving(LinkedList[] lines, String move1) {
        String[] move3 = move1.split(" ");
        int move = Integer.parseInt(move3[1]);
        int from = Integer.parseInt(move3[3])-1;
        int to = Integer.parseInt(move3[5])-1;
        for (int i = 0; i < move; i++) {
            lines[to].push(lines[from].pop());
        }
    }
    private static void moving9001(LinkedList<Character>[] lines, String move1) {
        String[] move3 = move1.split(" ");
        LinkedList<Character> reserve = new LinkedList<>();
        int move = Integer.parseInt(move3[1]);
        int from = Integer.parseInt(move3[3])-1;
        int to = Integer.parseInt(move3[5])-1;
        for (int i = 0; i < move; i++) {
            reserve.push(lines[from].pop());
        }
        for (int i = 0; i < move; i++) {
            lines[to].push(reserve.pop());
        }
    }
}
