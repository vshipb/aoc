package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class Day19Test {

    Puzzle p = new Day19();

    String input = "Blueprint 1: Each ore robot costs 4 ore." +
            " Each clay robot costs 2 ore." +
            " Each obsidian robot costs 3 ore and 14 clay." +
            " Each geode robot costs 2 ore and 7 obsidian." +
            "\n" +
            "Blueprint 2:" +
            " Each ore robot costs 2 ore." +
            " Each clay robot costs 3 ore." +
            " Each obsidian robot costs 3 ore and 8 clay." +
            " Each geode robot costs 3 ore and 12 obsidian.";

    String input2 = "Blueprint 1: Each ore robot costs 1 ore." +
            " Each clay robot costs 1 ore." +
            " Each obsidian robot costs 1 ore and 1 clay." +
            " Each geode robot costs 1 ore and 1 obsidian.";
    @Test
    public void part1(){
        Assertions.assertEquals("33", p.part1(input));
    }

    @Test
    public void part2(){
        Assertions.assertEquals("58", p.part2(input));
    }

    @Test
    public void testPriority() {
        Day19.Blueprint blueprint = new Day19.Blueprint(input2);
        Day19.Me me = new Day19.Me(blueprint);
        List<Day19.Robot> toBuildRobots = new ArrayList<>();

        for (int i = 0; i < 24; i++) {
            while (me.priority(blueprint.geodeRobot, toBuildRobots) != null) {
                Day19.Robot robotType = me.priority(blueprint.geodeRobot, toBuildRobots);
                toBuildRobots.add(robotType);
            }
            for (Day19.Robot robotType : toBuildRobots) {
                me.craft(me.materials, robotType);
            }
            me.robots.forEach((robot, count) -> me.materials.put(robot, me.materials.get(robot) + count));
            for (Day19.Robot robotType : toBuildRobots) {
                me.robots.put(robotType, me.robots.get(robotType) + 1);
            }
            System.out.println(i + "," + toBuildRobots);
            System.out.println(me.robots);
            System.out.println(me.materials);
            toBuildRobots.clear();
        }
        System.out.println(me.materials.get(blueprint.geodeRobot));
    }

    @Test
    public void testMaxGeodes() {
        Day19.Blueprint blueprint = new Day19.Blueprint(input2);
        Day19.Me me = new Day19.Me(blueprint);
        System.out.println(Day19.maxGeodes(8, me.materials, me.robots, blueprint));
    }

    @Test
    public void testCombinations() {
        Day19.Blueprint blueprint = new Day19.Blueprint(input2);
        Day19.Me me = new Day19.Me(blueprint);
        me.materials.put(blueprint.oreRobot, 1);
        System.out.println(Day19.combinations(me.materials, blueprint));
    }

    @Test
    public void testCanBuild() {
        Day19.Blueprint blueprint = new Day19.Blueprint(input2);
        Day19.Me me = new Day19.Me(blueprint);
        me.materials.put(blueprint.oreRobot, 1);
        System.out.println(Day19.canBuild(0,0,1, 0,me.materials, blueprint));
    }
}
