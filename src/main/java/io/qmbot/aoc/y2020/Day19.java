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
        Map<Integer, String> base = base(rules);
        String regex = regex(0, allRules, base);
        int result = 0;
        for (String s : parts[1].split(REGEX_NEW_LINE)) {
            if (Pattern.matches(regex, s)) {
                result++;
            }
        }
        return String.valueOf(result);
    }

    @Override
    public String part2(String input) {
        String[] parts = input.split(REGEX_EMPTY_LINE);
        String[] rules = parts[0].split(REGEX_NEW_LINE);
        Map<Integer, Rule> allRules = new HashMap<>();
        Map<Integer, String> base = base(rules);
        base.put(8, " 42 | 42 8");
        base.put(11, " 42 31 | 42 11 31");
        String regex = regex(0, allRules, base);
        int result = 0;
        for (String s : parts[1].split(REGEX_NEW_LINE)) {
            if (Pattern.matches(regex, s)) {
                result++;
            }
        }
        return String.valueOf(result);
//        trueStrings(ready, 0, allRules, base);
//        ready.remove(ready.size() - 1);
//        int result = 0;
//        String[] strings = parts[1].split(REGEX_NEW_LINE);
//        for (String string : strings) {
//            if (ready.contains(string)) {
//                result++;
//            }
//        }
    }

    private static String regex(int number, Map<Integer, Rule> allRules, Map<Integer, String> base) {
        addRule(number, allRules, base);
        Rule currentRule = allRules.get(number);
        return currentRule.toString();
    }

    private static void addRule(int number, Map<Integer, Rule> allRules, Map<Integer, String> base) {
        Rule rule = new Rule(number);
        addDescription(number, rule, allRules, base);
        allRules.put(number, rule);
    }

    private static void addDescription(int number, Rule rule, Map<Integer, Rule> allRules, Map<Integer, String> base) {
        String[] split = base.get(number).split(" ");
        if (number == 8) {
            rule.setRules(listRules(split, 1, 2), allRules, base);
        } else if (number == 11) {
            rule.setRules(listRules(split, 1, 3), allRules, base);
        } else if (split[1].contains("\"")) {
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
            if (number == 8) {
                StringBuilder stringRules = new StringBuilder();
                for (Rule rule : rules) {
                    stringRules.append(rule.toString());
                }
                return "(" + stringRules + ")+";
            }
            if (number == 11) {
                return "(" + (rules.get(0)) + "{1}" + (rules.get(1)) + "{1})|(" +
                        (rules.get(0)) + "{2}" + (rules.get(1)) + "{2})|(" +
                        (rules.get(0)) + "{3}" + (rules.get(1)) + "{3})|(" +
                        (rules.get(0)) + "{4}" + (rules.get(1)) + "{4})|(" +
                        (rules.get(0)) + "{5}" + (rules.get(1)) + "{5})|(" +
                        (rules.get(0)) + "{6}" + (rules.get(1)) + "{6})|(" +
                        (rules.get(0)) + "{7}" + (rules.get(1)) + "{7})|(" +
                        (rules.get(0)) + "{8}" + (rules.get(1)) + "{8})|(" +
                        (rules.get(0)) + "{9}" + (rules.get(1)) + "{9})|(" +
                        (rules.get(0)) + "{10}" + (rules.get(1)) + "{10})";
            }
            if (rules == null) {
                return String.valueOf(c);
            }
            if (altRules == null || altRules.size() == 0) {
                StringBuilder stringRules = new StringBuilder();
                for (Rule rule : rules) {
                    stringRules.append(rule.toString());
                }
                return String.valueOf(stringRules);
            }
            StringBuilder stringRules = new StringBuilder();
            for (Rule rule : rules) {
                stringRules.append(rule.toString());
            }
            StringBuilder stringAltRules = new StringBuilder();
            for (Rule rule : altRules) {
                stringAltRules.append(rule.toString());
            }
            return "(" +
                    stringRules +
                    "|" +
                    stringAltRules +
                    ")";
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
