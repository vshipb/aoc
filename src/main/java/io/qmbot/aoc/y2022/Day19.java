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
        int result = 1;

        for (String string : input.split("\n")) {
            blueprintList.add(new Blueprint(string));
        }
        for (int i = 0; i < 3; i++) {
            Blueprint blueprint = blueprintList.get(i);
            int blueprintResult = star(blueprint, 32);
            result = result * blueprintResult;
        }

        return String.valueOf(result);
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
            needMaterials();
        }

        private void needMaterials() {
            Robot[] robots = new Robot[]{oreRobot, clayRobot, obsidianRobot, geodeRobot};
            for (Robot material : robots) {
                for (Robot robot : robots) {
                    int materialNeed = material.need;
                    if (robot.cost.get(material) != null) {
                        int costMaterial = robot.cost.get(material);
                        if (materialNeed < costMaterial) {
                            material.need = costMaterial;
                        }
                    }
                }
            }
        }
    }

    static class Robot {
        @Override
        public String toString() {
            return name;
        }

        Map<Robot, Integer> cost = new LinkedHashMap<>();
        String name;
        int need = 0;

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
        PriorityQueue<PriorityState> frontier = new PriorityQueue<>();
        frontier.add(new PriorityState(State.createInitial(blueprint, time), 0));
        int newGeode;
        while (!frontier.isEmpty()) {
            PriorityState current = frontier.poll();
            if (current.state.robots.get(blueprint.clayRobot) == 4) {
                int j = 0;
            }
            if (current.state.remainingSteps == 0) {
                return current.state.materials.get(blueprint.geodeRobot);
            }
            List<Robot> needBuild = needBuildRobots(blueprint, current.state);
            for (Robot next : needBuild) {
                newGeode = current.state.materials.get(blueprint.geodeRobot)
                        + (current.state.robots.get(blueprint.geodeRobot) * current.state.remainingSteps);
                int priority = newGeode + heuristic(current.state, next, blueprint);
                PriorityState newState = new PriorityState(current.state.buildRobot(next), priority);
                if (newState.state != null) {
                    frontier.add(newState);
                }
            }
        }
        throw new IllegalStateException();
    }

    static List<Robot> needBuildRobots(Blueprint blueprint, State current) {
        List<Robot> needBuild = new ArrayList<>();
        Robot ore = blueprint.oreRobot;
        Robot clay = blueprint.clayRobot;
        Robot obsidian = blueprint.obsidianRobot;
        Robot geode = blueprint.geodeRobot;
        if (ore.need > current.robots.get(ore)) {
            needBuild.add(ore);
        }
        if (clay.need > current.robots.get(clay)) {
            needBuild.add(clay);
        }
        if (current.robots.get(clay) > 0 && obsidian.need > current.robots.get(obsidian)) {
            needBuild.add(obsidian);
        }
        if (current.robots.get(obsidian) > 0) {
            needBuild.add(geode);
        }
        return needBuild;
    }


    static int heuristic(State before, Robot next, Blueprint blueprint) {
        Robot ore = blueprint.oreRobot;
        Robot clay = blueprint.clayRobot;
        Robot obsidian = blueprint.obsidianRobot;
        Robot geode = blueprint.geodeRobot;
        State current = before.buildRobot(next);
        if (current == null) return 0;
        int time = current.remainingSteps;
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

    static class PriorityState implements Comparable<PriorityState> {
        State state;
        int priority;

        public PriorityState(State state, int priority) {
            this.state = state;
            this.priority = priority;
        }

        @Override
        public int compareTo(PriorityState o) {
            return -Integer.compare(this.priority, o.priority);
        }

    }

    static class State {
        Map<Robot, Integer> materials;
        Map<Robot, Integer> robots;
        int remainingSteps;

        @Override
        public String toString() {
            return "State{"
                    + "robots=" + robots
                    + ", remainingSteps=" + (remainingSteps) + '}';
        }

        static State createInitial(Blueprint blueprint, int remainingSteps) {
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
            return new State(remainingSteps, materials, robots);
        }

        private State(int remainingSteps, Map<Robot, Integer> materials, Map<Robot, Integer> robots) {
            this.remainingSteps = remainingSteps;
            this.materials = materials;
            this.robots = robots;
        }

        State buildRobot(Robot robot) {
            Map<Robot, Integer> newRobots = new HashMap<>(robots);
            Map<Robot, Integer> newMaterials = new HashMap<>(materials);
            int time = 1;
            int cost;
            int have;
            for (Robot material : materials.keySet()) {
                if (robot.cost.get(material) != null) {
                    cost = robot.cost.get(material);
                    have = newMaterials.get(material);
                    while (cost > have) {
                        robots.forEach((r, count) -> newMaterials.put(r, newMaterials.get(r) + count));
                        have = newMaterials.get(material);
                        time++;
                    }
                }
            }

            if(time > remainingSteps) {
                Map<Robot, Integer> newMaterialsWithoutBuildRobot = new HashMap<>(materials);
                robots.forEach((r, count) ->
                        newMaterialsWithoutBuildRobot.put(r, newMaterialsWithoutBuildRobot.get(r) + (count * remainingSteps)));
                return new State(0, newMaterialsWithoutBuildRobot, robots);
            }
            robot.cost.forEach((key, value) -> newMaterials.put(key, newMaterials.get(key) - value));
            robots.forEach((r, count) -> newMaterials.put(r, newMaterials.get(r) + count));

            newRobots.put(robot, robots.get(robot) + 1);
            int newTime = remainingSteps - time;

            return new State(newTime, newMaterials, newRobots);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            State state = (State) o;

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
