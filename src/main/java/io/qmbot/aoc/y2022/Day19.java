package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day19 implements Puzzle {
    @Override
    public String part1(String input) {
        List<Blueprint> blueprintList = new ArrayList<>();

        for (String string : input.split("\r\n")) {
            blueprintList.add(new Blueprint(string));
        }

        int time = 0;
        List<Integer> bestGeode = new ArrayList<>();

        for (Blueprint blueprint : blueprintList) {
            Me me = new Me(blueprint);
            List<Robot> robotTypes = Arrays.asList(blueprint.oreRobot, blueprint.clayRobot, blueprint.obsidianRobot, blueprint.geodeRobot);
            while (time < 24) {
                Robot robotType = me.priority(blueprint.geodeRobot);
                if (robotType != null) {
                    me.craft(robotType);
                }
                me.robots.forEach((robot, count) -> me.materials.put(robot, me.materials.get(robot) + count));
                if (robotType != null) {
                    me.robots.put(robotType, me.robots.get(robotType) + 1);
                }
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

        boolean isEnoughMaterials(Robot robot) {
            return robot.cost.entrySet().stream().allMatch(entry -> materials.get(entry.getKey()) >= entry.getValue());
        }

        Robot needRobot(Robot robot) {
            for (Robot material : robot.cost.keySet()) {
                if (materials.get(material) + (robots.get(material) * 2) < robot.cost.get(material)) {
                    return material;
                }
            }
            return null;
        }

        void craft(Robot robot) {
            robot.cost.forEach((key, value) -> materials.put(key, materials.get(key) - value));
        }

        Robot priority(Robot robot) {
            while (robot != null) {
                if (isEnoughMaterials(robot)) {
                    return robot;
                }
                robot = needRobot(robot);
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
            this.oreRobot = new Robot();
            oreRobot.cost.put(oreRobot, Integer.parseInt(words[6]));
            this.clayRobot = new Robot();
            clayRobot.cost.put(oreRobot, Integer.parseInt(words[12]));
            this.obsidianRobot = new Robot();
            obsidianRobot.cost.put(oreRobot, Integer.parseInt(words[18]));
            obsidianRobot.cost.put(clayRobot, Integer.parseInt(words[21]));
            this.geodeRobot = new Robot();
            geodeRobot.cost.put(oreRobot, Integer.parseInt(words[27]));
            geodeRobot.cost.put(obsidianRobot, Integer.parseInt(words[30]));
        }
    }

    static class Robot {
        Map<Robot, Integer> cost;

        public Robot() {
            this.cost = new HashMap<>();
        }
    }
}
