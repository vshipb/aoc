package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class Day19 implements Puzzle {
    @Override
    public String part1(String input) {
        List<Blueprint> blueprintList = new ArrayList<>();

        for (String string : input.split("\n")) {
            blueprintList.add(new Blueprint(string));
        }

        int time = 0;
        List<Integer> bestGeode = new ArrayList<>();
        List<Robot> toBuildRobots = new ArrayList<>();

        for (Blueprint blueprint : blueprintList) {
            Me me = new Me(blueprint);
            List<Robot> robotTypes = Arrays.asList(blueprint.oreRobot, blueprint.clayRobot, blueprint.obsidianRobot, blueprint.geodeRobot);
            while (time < 24) {
                int k = 0;
                //Robot robotType = me.priority(blueprint.geodeRobot);
                while (me.priority(blueprint.geodeRobot, toBuildRobots) != null) {
                    Robot robotType = me.priority(blueprint.geodeRobot, toBuildRobots);
                    toBuildRobots.add(robotType);
                }

                //if (robotType != null) {
                for (Robot robotType : toBuildRobots) {
                    me.craft(me.materials, robotType);
                }
                //}
                me.robots.forEach((robot, count) -> me.materials.put(robot, me.materials.get(robot) + count));
                //if (robotType != null) {
                for (Robot robotType : toBuildRobots) {
                    me.robots.put(robotType, me.robots.get(robotType) + 1);
                }
                //}
                toBuildRobots.clear();
                time++;
            }
            bestGeode.add(me.materials.get(robotTypes.get(3)));
            time = 0;
        }
        int j = 0;
        return "";
    }

    @Override
    public String part2(String input) {
        return "";
    }

    static class Me {
        Map<Robot, Integer> robots = new HashMap<>();
        Map<Robot, Integer> materials = new HashMap<>();

        public Me(Blueprint blueprint) {
            this.robots.put(blueprint.oreRobot, 1);
            this.robots.put(blueprint.clayRobot, 0);
            this.robots.put(blueprint.obsidianRobot, 0);
            this.robots.put(blueprint.geodeRobot, 0);

            this.materials.put(blueprint.oreRobot, 0);
            this.materials.put(blueprint.clayRobot, 0);
            this.materials.put(blueprint.obsidianRobot, 0);
            this.materials.put(blueprint.geodeRobot, 0);
        }

        boolean isEnoughMaterials(Robot robot, List<Robot> toBuildRobots) {
            Map<Robot, Integer> materials = new HashMap<>(this.materials);
            for (Robot robot1 : toBuildRobots) {
                craft(materials, robot1);
            }
            return robot.cost.entrySet().stream().allMatch(entry -> materials.get(entry.getKey()) >= entry.getValue());
        }

        Robot needRobot(Robot robot, List<Robot> toBuildRobots) {
            Map<Robot, Integer> materials = new HashMap<>(this.materials);
            for (Robot robot1 : toBuildRobots) {
                craft(materials, robot1);
            }
            for (Robot material : robot.cost.keySet()) {
                int m = (int) toBuildRobots.stream().filter(r -> r.equals(material)).count();
                if (materials.get(material) + (robots.get(material) * 2) + m < robot.cost.get(material)) {
                    return material;
                }
            }
            return null;
        }

        void craft(Map<Robot, Integer> materials, Robot robot) {
            robot.cost.forEach((key, value) -> materials.put(key, materials.get(key) - value));
        }

        Robot priority(Robot robot, List<Robot> toBuildRobots) {
            for (int i = 0; i < 4; i++) {
                if (robot == null) return null;
                if (isEnoughMaterials(robot, toBuildRobots)) {
                    return robot;
                }
                robot = needRobot(robot, toBuildRobots);
            }
            return null;
        }
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
            return name + "{" + "cost=" + cost.values() + '}';
        }

        Map<Robot, Integer> cost = new LinkedHashMap<>();
        String name;

        public Robot(String name) {
            this.name = name;
        }
    }

    static Map<Robot, Integer> robotMap(int ore, int clay, int obsidian, int geode, Blueprint blueprint) {
        Map<Robot, Integer> map = new HashMap<>();
        map.put(blueprint.oreRobot, ore);
        map.put(blueprint.clayRobot, clay);
        map.put(blueprint.obsidianRobot, obsidian);
        map.put(blueprint.geodeRobot, geode);
        return map;
    }

    static int maxGeodesCanBuild(Map<Robot, Integer> materials, Robot geode) {
        return geode.cost.entrySet().stream().mapToInt(e -> materials.get(e.getKey())/e.getValue()).min().orElseThrow();
    }

    static int maxGeodes(int turn, Map<Robot, Integer> materials, Map<Robot, Integer> robots, Blueprint blueprint, Map<Robot, Integer> previousMaterials, Map<Robot, Integer> previousBuild){
        if (turn == 1) {
            return materials.get(blueprint.geodeRobot) + robots.get(blueprint.geodeRobot);
        }



        List<Map<Robot, Integer>> combinations = turn > 2 ? combinations(materials, blueprint, previousMaterials, previousBuild) :
            List.of(robotMap(0, 0, 0, maxGeodesCanBuild(materials, blueprint.geodeRobot), blueprint));
        //System.out.println("turn: " + turn + " Combinations: " + combinations.size());
        if (combinations.size() > 50) {
            System.out.println(turn);
        }
        ForkJoinPool p = new ForkJoinPool(4);

        return combinations.parallelStream().mapToInt(build -> {
            Map<Robot, Integer> m = materialsAfterBuild(materials, build);
            Map<Robot, Integer> r = new HashMap<>(robots);
            robots.forEach((robot, count) -> m.put(robot, m.get(robot) + count));
            build.forEach((rob, i) -> r.put(rob, r.get(rob) + i));
            return  maxGeodes(turn - 1, m, r, blueprint, materials, build);
            }).max().orElseThrow();
//        for (Map<Robot, Integer> build : combinations) {
////            if (turn == 5) {
////                System.out.println();
////            }
//
//
//            Map<Robot, Integer> m = materialsAfterBuild(materials, build);
//            Map<Robot, Integer> r = new HashMap<>(robots);
//            robots.forEach((robot, count) -> m.put(robot, m.get(robot) + count));
//            build.forEach((rob, i) -> r.put(rob, r.get(rob) + i));
//            int geodes = maxGeodes(turn - 1, m, r, blueprint, materials, build);
//            if (geodes > max) {
//                max = geodes;
//            }
//
//        }
//        return max;
    }

    static Map<Robot, Integer> materialsAfterBuild(Map<Robot, Integer> materials, Map<Robot, Integer> build) {
        Map<Robot, Integer> result = new HashMap<>(materials);
        build.forEach((r, i) -> r.cost.forEach((r1, c) -> result.put(r1, result.get(r1) - c * i)));
        return result;
    }

    static List<Map<Robot, Integer>> combinations(Map<Robot, Integer> materials, Blueprint blueprint, Map<Robot, Integer> previousMaterials, Map<Robot, Integer> previousBuild) {
        int ore = 0;
        int clay = 0;
        int obsidian = 0;
        int geode = 0;
        List<Map<Robot, Integer>> combinations = new ArrayList<>();
        while (canBuild(ore, clay, obsidian, geode, materials, blueprint)
            && (ore == 0 || !canBuild(previousBuild.get(blueprint.oreRobot) + 1, previousBuild.get(blueprint.clayRobot),
            previousBuild.get(blueprint.obsidianRobot), previousBuild.get(blueprint.geodeRobot), previousMaterials, blueprint))) {
            while (canBuild(ore, clay, obsidian, geode, materials, blueprint)
                && (ore == 0 || !canBuild(previousBuild.get(blueprint.oreRobot), previousBuild.get(blueprint.clayRobot) + 1,
                previousBuild.get(blueprint.obsidianRobot), previousBuild.get(blueprint.geodeRobot), previousMaterials, blueprint))) {

                while (canBuild(ore, clay, obsidian, geode, materials, blueprint)
                    && (ore == 0 || !canBuild(previousBuild.get(blueprint.oreRobot), previousBuild.get(blueprint.clayRobot),
                    previousBuild.get(blueprint.obsidianRobot) + 1, previousBuild.get(blueprint.geodeRobot), previousMaterials, blueprint))) {

                    while (canBuild(ore, clay, obsidian, geode, materials, blueprint)
                        && (ore == 0 || !canBuild(previousBuild.get(blueprint.oreRobot), previousBuild.get(blueprint.clayRobot),
                        previousBuild.get(blueprint.obsidianRobot), previousBuild.get(blueprint.geodeRobot) + 1, previousMaterials, blueprint))) {
                          combinations.add(robotMap(ore,clay, obsidian, geode, blueprint));
                        geode++;
                    }
                    geode = 0;
                    obsidian++;
                }
                obsidian = 0;
                clay++;
            }
            clay = 0;
            ore++;
        }
        return combinations;
    }

    static boolean canBuild(int ore, int clay, int obsidian, int geode, Map<Robot, Integer> materials, Blueprint blueprint) {
        if (ore > 2 || clay > 2 || obsidian > 2) return false;
        Robot oreRobot = blueprint.oreRobot;
        Robot clayRobot = blueprint.clayRobot;
        Robot obsidianRobot = blueprint.obsidianRobot;
        Robot geodeRobot = blueprint.geodeRobot;

        Map<Robot, Integer> required = new HashMap<>();
        required.put(oreRobot, oreRobot.cost.get(oreRobot) * ore
                + clayRobot.cost.get(oreRobot) * clay
                + obsidianRobot.cost.get(oreRobot) * obsidian
                + geodeRobot.cost.get(oreRobot) * geode);
        required.put(clayRobot, obsidianRobot.cost.get(clayRobot) * obsidian);
        required.put(obsidianRobot, geodeRobot.cost.get(obsidianRobot) * geode);
        required.put(geodeRobot, 0);

        for (Robot robot : materials.keySet()) {
            if (materials.get(robot) < required.get(robot)) return false;
        }
        return true;
    }
}
