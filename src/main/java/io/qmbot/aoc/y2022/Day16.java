package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class Day16 implements Puzzle {
    @Override
    public Integer part1(String input) {
        Map<String, Valve> valves = new HashMap<>();
        List<String> flowMoreThanZero = new ArrayList<>();
        parseInput(valves, flowMoreThanZero, input);
        fillDestinations(valves);
        return star(valves, flowMoreThanZero, 30);
    }

    @Override
    public Integer part2(String input) {
        Map<String, Valve> valves = new HashMap<>();
        List<String> flowMoreThanZero = new ArrayList<>();
        parseInput(valves, flowMoreThanZero, input);
        fillDestinations(valves);
        return withElephant(flowMoreThanZero, valves, 26);
    }

    int withElephant(List<String> list, Map<String, Valve> valves, int time) {
        int size = 1 << list.size();
        int result = 0;
        int number;
        for (int i = 0; i < size; i++) {
            List<String> first = new ArrayList<>();
            List<String> second = new ArrayList<>();
            for (int j = 0; j < list.size(); j++) {
                if (((1 << j) & i) > 0) {
                    first.add(list.get(j));
                } else {
                    second.add(list.get(j));
                }
            }
            number = star(valves, first, time) + star(valves, second, time);
            if (result < number) result = number;
        }
        return result;
    }

    static void fillDestinations(Map<String, Valve> valves) {
        for (String valve : valves.keySet()) {
            findPath(valves, valve);
        }
    }

    static int heuristic(Map<String, Valve> valves, int time, List<String> needToOpen, Map<String, Integer> destination) {
        int result = 0;
        for (String v : needToOpen) {
            int stepsToDestination = time - destination.get(v) - 1;
            result += Math.max(0, stepsToDestination) * valves.get(v).flow;
        }
        return result;
    }

    static int star(Map<String, Valve> valves, List<String> flowMoreThanZero, int time) {
        String start = "AA";
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
                newGain = current.gain + (valves.get(next).flow * time);
                List<String> needToOpen = needToOpen(current.needToOpen, next);
                int priority = newGain + heuristic(valves, time, needToOpen, valves.get(next).destination);
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

    record PriorityValve(String name, int priority, int remainingSteps, int gain, List<String> needToOpen)
            implements Comparable<PriorityValve> {
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

        @Override
        public int compareTo(PriorityValve valve) {
            return -Integer.compare(this.priority, valve.priority);
        }
    }

    static final class Valve {
        private final List<String> tunnels;
        private final int flow;
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
