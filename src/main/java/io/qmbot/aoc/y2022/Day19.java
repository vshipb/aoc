package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class Day19 implements Puzzle {
    @Override
    public Integer part1(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).map(Blueprint::new)
                .mapToInt(blueprint -> star(blueprint, 24) * blueprint.id).sum();
    }

    @Override
    public Integer part2(String input) {
        return Arrays.stream(input.split(REGEX_NEW_LINE)).limit(3).map(Blueprint::new)
                .mapToInt(b -> star(b, 32)).reduce((a, b) -> a * b).orElseThrow();
    }

    static class Blueprint {
        int id;
        Robot oreRobot;
        Robot clayRobot;
        Robot obsidianRobot;
        Robot geodeRobot;
        List<Robot> robots;

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
            this.robots = List.of(oreRobot, clayRobot, obsidianRobot, geodeRobot);
            needMaterials();
        }

        private void needMaterials() {
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
            geodeRobot.need = Integer.MAX_VALUE;
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
            if (current.state.remainingSteps == 0) {
                return current.state.materials.get(blueprint.geodeRobot);
            }
            List<Robot> needBuild = needBuildRobots(current.state);
            for (Robot next : needBuild) {
                newGeode = current.state.materials.get(blueprint.geodeRobot)
                        + (current.state.robots.get(blueprint.geodeRobot) * current.state.remainingSteps);
                State state = current.state.buildRobot(next);
                int priority = newGeode + heuristic(state);
                PriorityState newState = new PriorityState(state, priority);
                if (newState.state != null) {
                    frontier.add(newState);
                }
            }
        }
        throw new IllegalStateException();
    }

    static List<Robot> needBuildRobots(State current) {
        return current.robots.keySet().stream().filter(r -> r.need > current.robots.get(r))
                .filter(r -> r.cost.entrySet().stream().allMatch((e) -> current.robots.get(e.getKey()) > 0)).collect(Collectors.toList());
    }


    static int heuristic(State current) {
        int time = current.remainingSteps;
        return (time * (time + 1) / 2);
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
            if (time > remainingSteps) {
                Map<Robot, Integer> newMaterialsWithoutBuildRobot = new HashMap<>(materials);
                robots.forEach((r, count) ->
                        newMaterialsWithoutBuildRobot.put(r, newMaterialsWithoutBuildRobot.get(r) + (count * remainingSteps)));
                return new State(0, newMaterialsWithoutBuildRobot, robots);
            }
            robot.cost.forEach((key, value) -> newMaterials.put(key, newMaterials.get(key) - value));
            robots.forEach((r, count) -> newMaterials.put(r, newMaterials.get(r) + count));
            newRobots.put(robot, robots.get(robot) + 1);
            return new State(remainingSteps - time, newMaterials, newRobots);
        }
    }
}
