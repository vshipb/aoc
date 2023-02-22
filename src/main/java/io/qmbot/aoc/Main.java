package io.qmbot.aoc;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, InstantiationException {
        for (int i = 1; i < 25; i++) {
            String formatted = String.format("io.qmbot.aoc.y2022.Day%02d", i);
            Class cls = Class.forName(formatted);
            Puzzle p = (Puzzle) cls.getDeclaredConstructor().newInstance();
            String path = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\22_"+i+".txt";
            String input = Files.readString(Paths.get(path));
            System.out.println("Day " + i + " answer");
            System.out.println(p.part1(input));
            System.out.println(p.part2(input));
        }
    }
}
