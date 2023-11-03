package io.qmbot.aoc.y2020;

import io.qmbot.aoc.Puzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;
public class Day19 implements Puzzle {
    @Override
    public String part1(String input) {
        String[] parts = input.split(REGEX_EMPTY_LINE);
        String[] rules = parts[0].split(REGEX_NEW_LINE);
        Map<Integer, Rule> allRules = new HashMap<>();

        List<String> ready = new ArrayList<>();
        ready.add("");
        Map<Integer, String> base = base(rules);
        trueStrings(ready, 0, allRules, base);
        ready.remove(ready.size() - 1);
        int result = 0;
        String[] strings = parts[1].split(REGEX_NEW_LINE);
        for (String string : strings) {
            if (ready.contains(string)) {
                result++;
            }
        }
        return String.valueOf(result);
    }

    @Override
    public String part2(String input) {
        String[] parts = input.split(REGEX_EMPTY_LINE);
        String[] rules = parts[0].split(REGEX_NEW_LINE);
        List<String> ready = new ArrayList<>();
        ready.add("");
        Map<Integer, Rule> allRules = new HashMap<>();
        Map<Integer, String> base = base(rules);
        base.put(8, " 42 | 42 8");
        base.put(11, " 42 31 | 42 11 31");
        trueStrings(ready, 0, allRules, base);
        ready.remove(ready.size() - 1);
        int result = 0;
        String[] strings = parts[1].split(REGEX_NEW_LINE);
        for (String string : strings) {
            if (ready.contains(string)) {
                result++;
            }
        }
        return String.valueOf(result);
    }

    private static void trueStrings(List<String> ready, int number, Map<Integer, Rule> allRules, Map<Integer, String> base) {
        if (!allRules.containsKey(number)) {
            addRule(number, allRules, base);
        }
        Rule currentRule = allRules.get(number);
        if (currentRule.c != '\0') {
            ready.replaceAll(s -> s + currentRule.c);
        }
        if (currentRule.rules != null) {
            List<String> addReady = new ArrayList<>(ready);
            for (Rule rule : currentRule.rules) {
                trueStrings(addReady, rule.number, allRules, base);
            }
            List<String> addReady2 = new ArrayList<>(ready);
            if (currentRule.altRules != null) {
                for (Rule rule : currentRule.altRules) {
                    trueStrings(addReady2, rule.number, allRules, base);
                }
            }
            ready.clear();
            ready.addAll(addReady);
            ready.addAll(addReady2);
        }
    }

    private static void addRule(int number, Map<Integer, Rule> allRules, Map<Integer, String> base) {
        Rule rule = new Rule(number);
        if (rule.rules == null && rule.c == '\0') {
            addDescription(number, rule, allRules, base);
        }
        allRules.put(number, rule);
    }

    private static void addDescription(int number, Rule rule, Map<Integer, Rule> allRules, Map<Integer, String> base) {
        String[] split = base.get(number).split(" ");
        if (split[1].contains("\"")) {
            rule.setC(split[1].charAt(1));
        } else {
            int index = Arrays.asList(split).indexOf("|");
            rule.setRules(listRules(split, 1, index > 0 ? index : split.length), allRules, base);
            rule.setAltRules(index > 0 ? listRules(split, index + 1, split.length) : new ArrayList<>(), allRules, base);
        }
    }

    private static Map<Integer, String> base(String[] rules) {
        Map<Integer, String> base = new HashMap<>();
        for (String string : rules) {
            String[] rule = string.split(":");
            base.put(Integer.valueOf(rule[0]), rule[1]);
        }
        return base;
    }

//    private static Map<Integer, Rule> allRules(String[] rules, Map<Integer, String> base) {
//        Map<Integer, Rule> allRules = new HashMap<>();
//        for (String rule : rules) {
//            String[] split = rule.split(" ");
//            int number = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
//            Rule currentRule = allRules.computeIfAbsent(number, Rule::new);
//            if (split[1].contains("\"")) {
//                currentRule.setC(split[1].charAt(1));
//            } else {
//                int index = Arrays.asList(split).indexOf("|");
//                currentRule.setRules(listRules(split, 1, index > 0 ? index : split.length), allRules,);
//                currentRule.setAltRules(index > 0 ? listRules(split, index + 1, split.length) : new ArrayList<>(), allRules);
//            }
//        }
//        return allRules;
//    }

    private static List<Integer> listRules(String[] split, int start, int end) {
        List<Integer> listRules = new ArrayList<>();
        for (int i = start; i < end; i++) {
            listRules.add(Integer.valueOf(split[i]));
        }
        return listRules;
    }

    private static class Rule {
        int number;
        List<Rule> rules;
        List<Rule> altRules;
        char c;

        @Override
        public String toString() {
            if (rules == null) {
                return "Rule{" +
                        "c=" + c +
                        '}';
            }
            if (altRules == null) {
                return "Rule{" +
                        "number=" + number +
                        ", rules=" + rules +
                        '}';
            }
            return "Rule{" +
                    "number=" + number +
                    ", rules=" + rules +
                    ", altRules=" + altRules +
                    '}';
        }

        public Rule(int number) {
            this.number = number;
        }

        public void setRules(List<Integer> rules, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            this.rules = listRules(rules, allRules, base);
        }

        public void setAltRules(List<Integer> rules, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            this.altRules = listRules(rules, allRules, base);
        }

        private static List<Rule> listRules(List<Integer> numbers, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            List<Rule> listRules = new ArrayList<>();
            for (int number : numbers) {
                if (!allRules.containsKey(number)) {
                    addRule(number, allRules, base);
                }
                listRules.add(allRules.get(number));
            }
            return listRules;
        }


        public char getC() {
            return c;
        }

        public void setC(char c) {
            this.c = c;
        }
    }
}
