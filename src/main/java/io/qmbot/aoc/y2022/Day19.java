package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;

public class Day19 implements Puzzle {
    @Override
    public String part1(String input) {
        List<Blueprint> blueprintList = new ArrayList<>();
        Map<Integer, Integer> result = new HashMap<>();

        for (String string : input.split("\n")) {
            blueprintList.add(new Blueprint(string));
        }
        for (Blueprint blueprint : blueprintList) {
            int blueprintResult = star(blueprint, 24);
            int qualityLevel = blueprintResult * blueprint.id;
            result.put(blueprint.id, qualityLevel);
        }
        int r = 0;
        for (int i : result.values()) {
            r += i;
        }
        return String.valueOf(r);
    }

    @Override
    public String part2(String input) {
        return "";
    }

    static class Blueprint {
        int id;
        Robot oreRobot;
        Robot clayRobot;
        Robot obsidianRobot;
        Robot geodeRobot;

        public Blueprint(String string) {
            String[] words = string.split(" ");
            this.id = Integer.parseInt(words[1].substring(0, words[1].length() - 1));
            this.oreRobot = new Robot("ore");
            oreRobot.cost.put(oreRobot, Integer.parseInt(words[6]));
            this.clayRobot = new Robot("clay");
            clayRobot.cost.put(oreRobot, Integer.parseInt(words[12]));
            this.obsidianRobot = new Robot("obsidian");
            obsidianRobot.cost.put(oreRobot, Integer.parseInt(words[18]));
            obsidianRobot.cost.put(clayRobot, Integer.parseInt(words[21]));
            this.geodeRobot = new Robot("geode");
            geodeRobot.cost.put(oreRobot, Integer.parseInt(words[27]));
            geodeRobot.cost.put(obsidianRobot, Integer.parseInt(words[30]));
        }
    }

    static class Robot {
        @Override
        public String toString() {
            return name;
        }

        Map<Robot, Integer> cost = new LinkedHashMap<>();
        String name;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Robot robot = (Robot) o;

            return Objects.equals(name, robot.name);
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }

        public Robot(String name) {
            this.name = name;
        }
    }

    static Map<Robot, Integer> materialsAfterBuild(Map<Robot, Integer> materials, Map<Robot, Integer> build) {
        Map<Robot, Integer> result = new HashMap<>(materials);
        build.forEach((r, i) -> r.cost.forEach((r1, c) -> result.put(r1, result.get(r1) - c * i)));
        return result;
    }

    int star(Blueprint blueprint, int time) {
        String start = "ore";
        PriorityQueue<State> frontier = new PriorityQueue<>();
        Map<Robot, Integer> materials = new HashMap<>();
        Map<Robot, Integer> robots = new HashMap<>();
        robots.put(blueprint.oreRobot, 1);
        robots.put(blueprint.clayRobot, 0);
        robots.put(blueprint.obsidianRobot, 0);
        robots.put(blueprint.geodeRobot, 0);

        materials.put(blueprint.oreRobot, 0);
        materials.put(blueprint.clayRobot, 0);
        materials.put(blueprint.obsidianRobot, 0);
        materials.put(blueprint.geodeRobot, 0);
        frontier.add(new State(0, time, materials, robots));
        int newGeode;
        while (!frontier.isEmpty()) {
            State current = frontier.poll();

            if (current.remainingSteps == 0) {
                return current.materials.get(blueprint.geodeRobot);
            }
            List<Robot> canBuild = new ArrayList<>();
            canBuild.add(blueprint.oreRobot);
            canBuild.add(blueprint.clayRobot);
            canBuild.add(blueprint.obsidianRobot);
            canBuild.add(blueprint.geodeRobot);
            canBuild.add(null);
            for (Robot next : canBuild) {
                if (next == null
                        || next.cost.entrySet().stream().allMatch(entry -> current.materials.get(entry.getKey()) >= entry.getValue())) {
                    time = current.remainingSteps - 1;
                    newGeode = current.materials.get(blueprint.geodeRobot)
                            + (current.robots.get(blueprint.geodeRobot) * time);
                    int priority = newGeode + heuristic(current, next, blueprint);
                    State newState = current.buildRobot(next, priority);
                    if (!frontier.contains(newState)) {
                        frontier.add(newState);
                    }
                }
            }
        }
        throw new IllegalStateException();
    }

    static int heuristic(State current, Robot next, Blueprint blueprint) {
        Robot ore = blueprint.oreRobot;
        Robot clay = blueprint.clayRobot;
        Robot obsidian = blueprint.obsidianRobot;
        Robot geode = blueprint.geodeRobot;
        int time = current.remainingSteps;
        int timeClay = 0;
        int timeObsidian = 0;
        int timeGeode = 0;
        if (current.robots.get(clay) == 0) {
            int have = current.materials.get(ore);
            while (((timeClay * (timeClay - 1)) / 2) + have < clay.cost.get(ore)) {
                timeClay++;
            }
        }
        if (current.robots.get(obsidian) == 0) {
            int haveOre = current.materials.get(ore);
            int haveClay = current.materials.get(clay);
            while (((timeObsidian * (timeObsidian - 1)) / 2) + haveOre < obsidian.cost.get(ore)
                    || ((timeObsidian * (timeObsidian - 1)) / 2) + haveClay < obsidian.cost.get(clay)) {
                timeObsidian++;
            }
        }
        if (current.robots.get(geode) == 0) {
            int haveOre = current.materials.get(ore);
            int haveObsidian = current.materials.get(obsidian);
            while (((timeGeode * (timeGeode - 1)) / 2) + haveOre < geode.cost.get(ore)
                    || ((timeGeode * (timeGeode - 1)) / 2) + haveObsidian < geode.cost.get(obsidian)) {
                timeGeode++;
            }
        }
        time = time - timeClay - timeObsidian - timeGeode;
        if (time < 0) {
            int j = 0;
        }
        return (time * (time - 1) / 2);
    }

    class State implements Comparable<State> {
        Map<Robot, Integer> materials;
        Map<Robot, Integer> robots;
        int priority;
        int remainingSteps;

        public boolean haveClay(Blueprint blueprint) {
            return robots.get(blueprint.clayRobot) > 0;
        }

        public boolean haveObsidian(Blueprint blueprint) {
            return robots.get(blueprint.obsidianRobot) > 0;
        }

        public boolean haveGeode(Blueprint blueprint) {
            return robots.get(blueprint.geodeRobot) > 0;
        }

        @Override
        public String toString() {
            return "State{"
                    + "robots=" + robots
                    + ", priority=" + priority
                    + ", remainingSteps=" + (remainingSteps) + '}';
        }

        public State(int priority, int remainingSteps, Map<Robot, Integer> materials, Map<Robot, Integer> robots) {
            this.priority = priority;
            this.remainingSteps = remainingSteps;
            this.materials = materials;
            this.robots = robots;
        }

        State buildRobot(Robot robot, int priority) {
            Map<Robot, Integer> newRobots = new HashMap<>(robots);
            Map<Robot, Integer> newMaterials = new HashMap<>(materials);
            if (robot != null) {
                newRobots.put(robot, robots.get(robot) + 1);
                robot.cost.forEach((key, value) -> newMaterials.put(key, materials.get(key) - value));
            }
            robots.forEach((r, count) -> newMaterials.put(r, newMaterials.get(r) + count));
            return new State(priority, remainingSteps - 1, newMaterials, newRobots);
        }

        @Override
        public int compareTo(State state) {
            return -Integer.compare(this.priority, state.priority);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            State state = (State) o;

            if (priority != state.priority) return false;
            if (remainingSteps != state.remainingSteps) return false;
            if (!Objects.equals(materials, state.materials)) return false;
            return Objects.equals(robots, state.robots);
        }

        @Override
        public int hashCode() {
            int result = robots.hashCode();
            result = 31 * result + remainingSteps;
            return result;
        }
    }
}
