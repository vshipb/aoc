package io.qmbot.aoc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IOException,
            InvocationTargetException, IllegalAccessException, InstantiationException {
        for (int i = 17; i < 18; i++) {
            String formatted = String.format("io.qmbot.aoc.y2022.Day%02d", i);
            Class cls = Class.forName(formatted);
            Puzzle p = (Puzzle) cls.getDeclaredConstructor().newInstance();

            ClassLoader classLoader = Main.class.getClassLoader();
            File file = new File(classLoader.getResource("ArinaResources/22_" + i + ".txt").getFile());

            String input = Files.readString(Path.of(file.getPath()));
            System.out.println("Day " + i + " answer");
            System.out.println(p.part1(input));
            System.out.println(p.part2(input));
        }
    }
}
