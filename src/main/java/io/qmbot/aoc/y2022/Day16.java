package io.qmbot.aoc.y2022;

import org.paukov.combinatorics3.Generator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Day16 {
    private static String PATH = "C:\\Users\\arina\\IdeaProjects\\untitled2\\src\\main\\resources\\22_16.txt";

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.lines(Paths.get(PATH)).collect(Collectors.toList());
        System.out.println(part1(lines));
        System.out.println(part2(lines));
    }

    private static String part1(List<String> strings) {
        Map<String, Valve> valves = new HashMap<>();
        List<String> flowMoreThanZero = new ArrayList<>();
        for (String string : strings) {
            String[] splitted = string.split(" ");
            String rate = splitted[4];
            String name = splitted[1];
            List<String> tunnels = new ArrayList<>();
            int i = splitted.length - 1;
            while (!splitted[i].equals("valves") && !splitted[i].equals("valve")) {
                String nameKey = splitted[i].replaceAll(",", "");
                tunnels.add(nameKey);
                i--;
            }
            valves.put(name, new Valve(Integer.parseInt(rate.substring(5, (rate.length() - 1))), tunnels));
            if (valves.get(name).flow != 0) {
                flowMoreThanZero.add(name);
            }
        }
        List<String> example = new ArrayList<>();
        example.add("DD");
        example.add("BB");
        example.add("JJ");
        example.add("HH");
        example.add("EE");
        example.add("CC");



        List<String> blocked = new ArrayList<>();
        System.out.println(closestMove("AA", "AA", valves, blocked));
        int result = pressure(valves, blocked, flowMoreThanZero);

        AtomicInteger bestResult = new AtomicInteger();

//        List<List<String>> combinations = Generator.permutation(flowMoreThanZero.toArray(new String[0]))
//                .simple().stream().toList();

//        Generator.permutation(flowMoreThanZero.toArray(new String[0]))
//                .simple()
//                .forEach(element -> {
//                    int newResult = 5;//pressure(valves, blocked, element);
//                    bestResult.set(Math.max(bestResult.get(), newResult));
//                });

//        for (List<String> combination : combinations){
//            int newResult = pressure(valves, blocked, combination);
//            bestResult = Math.max(bestResult, newResult);
//        }

        return String.valueOf(bestResult.get());
    }

    private static String part2(List<String> strings) {
        return "";
    }

    static class Valve {
        List<String> tunnels;
        int flow;
        boolean open = false;

        public Valve(int flow, List<String> tunnels) {
            this.flow = flow;
            this.tunnels = tunnels;
        }
    }
    private static int pressure(Map<String, Valve> valves,List<String> blocked, List<String> needValves) {
        int stepsOver = 30;
        int pressure;
        int relatingPressure = 0;
        int pressureOfOne;
        String start = needValves.get(0);
        stepsOver = stepsOver - (closestMove("AA", start, valves, blocked)) - 1;
        relatingPressure = relatingPressure + valves.get(start).flow;
        pressure = stepsOver * valves.get(start).flow;
        //System.out.println(stepsOver + " " + pressure + " " + relatingPressure);//
        for (int x = 1; x < needValves.size(); x++) {
            start = needValves.get(x);
            pressureOfOne = valves.get(start).flow;
            stepsOver = stepsOver - closestMove(needValves.get(x - 1), start, valves, blocked) - 1;
            if (stepsOver < 0) return 0;
            relatingPressure = relatingPressure + pressureOfOne;
            pressure = pressure + stepsOver * pressureOfOne;
            // System.out.println(stepsOver + " " + pressure + " " + relatingPressure);//
        }
        return pressure;
    }
    private static int closestMove(String nameStart, String nameStop, Map<String, Valve> valves, List<String> blocked1) {
        int steps = 1000;
        int bestSteps = 10000;
        String nameNow = nameStart;
        List<String> blocked = new ArrayList<>(blocked1);
        blocked.add(nameNow);

        if (blocked.contains(nameStop)) {
            steps = blocked.size() - 1;
            return steps;
        }

        for (String tunnel : valves.get(nameStart).tunnels) {

            if (!blocked.contains(tunnel)) {
                nameNow = tunnel;
                steps = closestMove(nameNow, nameStop, valves, blocked);
                if (nameNow.equals(nameStop)) {
                    return steps;
                }
            }
            if ((blocked.contains(tunnel) && (valves.get(nameStart).tunnels.size() == 1))) {
                steps = bestSteps;
            }
            if (steps < bestSteps) bestSteps = steps;
        }

        return bestSteps;
    }
}


