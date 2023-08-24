package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Day16 implements Puzzle {
    @Override
    public String part1(String input) {
        Map<String, Valve> valves = new HashMap<>();
        List<String> flowMoreThanZero = new ArrayList<>();

        parseInput(valves, flowMoreThanZero, input);
        fillDestinations(valves);

        return String.valueOf(star(valves, flowMoreThanZero));
    }

    @Override
    public String part2(String input) {
        return "";
    }

    static void fillDestinations(Map<String, Valve> valves) {
        for (String valve : valves.keySet()) {
            for (String v : valves.keySet()) {
                findPath(valves, valve);
            }
        }
    }

    static int heuristic(Map<String, Valve> valves, List<String> flowMoreThanZero, String start, int time) {
        int result = 0;
        for (String valve : flowMoreThanZero) {
            if (!valves.get(valve).open) {
                result += (time - valves.get(start).destination.get(valve) - 1) * valves.get(valve).flow;
            }
        }
        return result;
    }

    static int star(Map<String, Valve> valves, List<String> flowMoreThanZero) {
        String start = "AA";
        int time = 30;
        PriorityQueue<PriorityValve> frontier = new PriorityQueue<>();

        frontier.add(new PriorityValve(start, 0, time, 0, flowMoreThanZero));
        int newGain;

        while (!frontier.isEmpty()) {
            PriorityValve current = frontier.poll();

            List<String> canVisit = current.needToOpen.stream()
                    .filter(s -> current.remainingSteps - valves.get(current.name).destination.get(s) > 0).toList();

            if (canVisit.isEmpty())
                return current.gain;
            for (String next : canVisit) {
                time = current.remainingSteps - valves.get(current.name).destination.get(next) - 1;
                if (time <= 0) {
                    continue;
                }
                newGain = current.gain + (valves.get(next).flow * time);
                List<String> needToOpen = needToOpen(current.needToOpen, next);
                int priority = newGain + heuristic(valves, needToOpen, current.name, time);
                frontier.add(new PriorityValve(next, priority, time, newGain, needToOpen));
            }
        }
        return 0;
    }

    static List<String> needToOpen(List<String> currentToOpen, String current) {
        List<String> result = new ArrayList<>(currentToOpen);
        result.remove(current);
        return result;
    }

    static class PriorityValve implements Comparable<PriorityValve> {
        final String name;
        final int priority;
        final int remainingSteps;
        final int gain;
        final List<String> needToOpen;

        @Override
        public String toString() {
            return "PriorityValve{"
                    + "name='" + name + '\''
                    + ", priority=" + priority
                    + ", remainingSteps=" + remainingSteps
                    + ", gain=" + gain
                    + ", needToOpen=" + needToOpen
                    + '}';
        }

        public PriorityValve(String name, int priority, int remainingSteps, int gain, List<String> needToOpen) {
            this.name = name;
            this.priority = priority;
            this.remainingSteps = remainingSteps;
            this.gain = gain;
            this.needToOpen = needToOpen;
        }

        @Override
        public boolean equals(Object o) {
            PriorityValve v = (PriorityValve) o;
            if (Objects.equals(this.name, v.name)) return true;
            if (getClass() != o.getClass()) return false;

            return this.priority == v.priority;
        }

        @Override
        public int compareTo(PriorityValve valve) {
            return -Integer.compare(this.priority, valve.priority);
        }
    }

    static final class Valve {
        private final List<String> tunnels;
        private final int flow;
        private boolean open = false;
        Map<String, Integer> destination = new HashMap<>();

        Valve(List<String> tunnels, int flow) {
            this.tunnels = tunnels;
            this.flow = flow;
        }

        @Override
        public String toString() {
            return "Valve["
                    + "tunnels=" + tunnels + ", "
                    + "flow=" + flow + ']';
        }
    }


    static void parseInput(Map<String, Valve> valves, List<String> flowMoreThanZero, String input) {
        List<String> strings = input.lines().toList();
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
            valves.put(name, new Valve(tunnels, Integer.parseInt(rate.substring(5, (rate.length() - 1)))));
            if (valves.get(name).flow != 0) {
                flowMoreThanZero.add(name);
            }
        }
    }

    static void findPath(Map<String, Valve> valves, String start) {
        Queue<String> frontier = new LinkedList<>();
        Set<String> reached = new HashSet<>();
        reached.add(start);
        frontier.add(start);
        String current;
        int steps = 0;
        valves.get(start).destination.put(start, steps);
        while (!frontier.isEmpty()) {
            current = frontier.poll();
            for (String next : valves.get(current).tunnels) {
                if (!reached.contains(next)) {
                    valves.get(start).destination.put(next, valves.get(start).destination.get(current) + 1);
                    frontier.add(next);
                    reached.add(next);
                }
            }
        }
    }
}
