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

        List<Blueprint> blueprintList = new ArrayList<>();
        Map<Integer, Integer> result = new HashMap<>();

        for (String string : input.split("\n")) {
            blueprintList.add(new Blueprint(string));
        }
        for (Blueprint blueprint : blueprintList) {
            int blueprintResult = star(blueprint, 32);
            int qualityLevel = blueprintResult * blueprint.id;
            result.put(blueprint.id, qualityLevel);
        }
        int r = 0;
        for (int i : result.values()) {
            r += i;
        }
        return String.valueOf(r);
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

    static int star(Blueprint blueprint, int time) {
        PriorityQueue<State> frontier = new PriorityQueue<>();
        Map<Robot, Integer> materials = new HashMap<>();
        Map<Robot, Integer> robots = new HashMap<>();
        startRobotsAndMaterials(materials, robots, blueprint);
        frontier.add(new State(0, time, materials, robots));
        int newGeode;
        while (!frontier.isEmpty()) {
            State current = frontier.poll();
            if (current.remainingSteps == 0) {
                return current.materials.get(blueprint.geodeRobot);
            }
            List<Robot> needBuild = needBuildRobots(blueprint, current.remainingSteps, current);
            for (Robot next : needBuild) {
                if (next == null || canBuild(next, current)) {
                    newGeode = current.materials.get(blueprint.geodeRobot)
                            + (current.robots.get(blueprint.geodeRobot) * current.remainingSteps);
                    int priority = newGeode + heuristic(current, next, blueprint);
                    State newState = current.buildRobot(next, priority);
                    frontier.add(newState);
                }
            }
        }
        throw new IllegalStateException();
    }

    static void startRobotsAndMaterials(Map<Robot, Integer> materials, Map<Robot, Integer> robots, Blueprint blueprint) {
        robots.put(blueprint.oreRobot, 1);
        robots.put(blueprint.clayRobot, 0);
        robots.put(blueprint.obsidianRobot, 0);
        robots.put(blueprint.geodeRobot, 0);

        materials.put(blueprint.oreRobot, 0);
        materials.put(blueprint.clayRobot, 0);
        materials.put(blueprint.obsidianRobot, 0);
        materials.put(blueprint.geodeRobot, 0);
    }

    static boolean canBuild(Robot next, State current) {
        return next.cost.entrySet().stream().allMatch(entry -> current.materials.get(entry.getKey()) >= entry.getValue());
    }

    static List<Robot> needBuildRobots(Blueprint blueprint, int remainingSteps, State current) {
        // если хватает роботов на максимально нужное кол-во материалов, то не делать робота
        List<Robot> needBuild = new ArrayList<>();
        if (remainingSteps >= 1) {
            needBuild.add(null);
        }
        if (remainingSteps >= 2) {
            needBuild.add(blueprint.geodeRobot);
        }
        if (remainingSteps >= 3) {
            if (needBuildRobot(current, blueprint.obsidianRobot)) {
                needBuild.add(blueprint.obsidianRobot);
            }
            if (needBuildRobot(current, blueprint.clayRobot)) {
                needBuild.add(blueprint.clayRobot);
            }
            if (needBuildRobot(current, blueprint.oreRobot)) {
                needBuild.add(blueprint.oreRobot);
            }
        }
        return needBuild;
    }

    static boolean needBuildRobot(State current, Robot material) {
        for (Robot robot : current.robots.keySet()) {
            if (robot.cost.get(material) != null && robot.cost.get(material) >= current.robots.get(material))
                return true;
        }
        return false;
    }

    static int heuristic(State before, Robot next, Blueprint blueprint) {
        Robot ore = blueprint.oreRobot;
        Robot clay = blueprint.clayRobot;
        Robot obsidian = blueprint.obsidianRobot;
        Robot geode = blueprint.geodeRobot;
        State current = before.buildRobot(next, 0);
        int time = before.remainingSteps;
        Robot[] materials = {ore, clay, obsidian, geode};
        for (int i = 0; i < materials.length; i++) {
            Robot currentMaterial = materials[i];
            if (current.robots.get(currentMaterial) == 0) {
                Robot nextMaterial = materials[i - 1];
                time = time - timeBeforeRobot(current.robots.get(nextMaterial), current.materials.get(nextMaterial), currentMaterial.cost.get(nextMaterial));
            }
        }
        if (time <= 0) {
            return 0;
        }
        return (time * (time - 1) / 2);
    }

    static int timeBeforeRobot(int haveRobots, int haveMaterials, int needMaterials) {
        int time = 0;
        while (((time * (time - 1)) / 2) + haveMaterials + (haveRobots * time) < needMaterials) {
            time++;
        }
        return time + 1;
    }

    static class State implements Comparable<State> {
        Map<Robot, Integer> materials;
        Map<Robot, Integer> robots;
        int priority;
        int remainingSteps;

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
