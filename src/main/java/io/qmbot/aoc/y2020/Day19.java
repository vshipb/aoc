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
    }

    private static String regex(int number, Map<Integer, Rule> allRules, Map<Integer, String> base) {
        addRule(number, allRules, base);
        Rule currentRule = allRules.get(number);
        return currentRule.toString();
    }

    Rule parseRule(String str, Map<Integer, Rule> allRules) {
        String[] split = str.split(" ");
        int number = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
        if (number == 8) {
            return new Rule8(split, allRules, base);
        } else if (number == 11) {
            return new Rule11(split, allRules, base);
        } else if (split[1].contains("\"")) {
            return new CharRule(split[1].charAt(1));
        } else {
            int index = Arrays.asList(split).indexOf("|");
            if (index > 0) {
                String[] splitted = str.split("|");
                return new OrRule(parseRule(splitted[0].trim(), allRules), parseRule(splitted[1].trim(), allRules));
            } else {
                return new SequenceRule(split, allRules, base);
            }
        }
    }

//    private static void addRule(int number, Map<Integer, Rule> allRules, Map<Integer, String> base) {
//        String[] split = base.get(number).split(" ");
//        if (number == 8) {
//            Rule8 rule = new Rule8(split, allRules, base);
//            allRules.put(number, rule);
//        } else if (number == 11) {
//            Rule11 rule = new Rule11(split, allRules, base);
//            allRules.put(number, rule);
//        } else if (split[1].contains("\"")) {
//            CharRule rule = new CharRule(split[1].charAt(1));
//            allRules.put(number, rule);
//        } else {
//            int index = Arrays.asList(split).indexOf("|");
//            if (index > 0) {
//                OrRule rule = new OrRule(index, split, allRules, base);
//                allRules.put(number, rule);
//            } else {
//                SequenceRule rule = new SequenceRule(split, allRules, base);
//                allRules.put(number, rule);
//            }
//        }
//    }

    private static Map<Integer, String> base(String[] rules) {
        Map<Integer, String> base = new HashMap<>();
        for (String string : rules) {
            String[] rule = string.split(":");
            base.put(Integer.valueOf(rule[0]), rule[1]);
        }
        return base;
    }

    private static List<Integer> listNumbers(String[] split, int start, int end) {
        List<Integer> listNumbers = new ArrayList<>();
        for (int i = start; i < end; i++) {
            listNumbers.add(Integer.valueOf(split[i]));
        }
        return listNumbers;
    }
    static String buildStringFromRules(List<Rule> rules) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Rule rule : rules) {
            stringBuilder.append(rule.toString());
        }
        return String.valueOf(stringBuilder);
    }

    private abstract static class Rule {

        abstract String regex();

         static List<Rule> listRules(List<Integer> numbers, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            List<Rule> listRules = new ArrayList<>();
            for (int number : numbers) {
                if (!allRules.containsKey(number)) {
                    addRule(number, allRules, base);
                }
                listRules.add(allRules.get(number));
            }
            return listRules;
        }
    }

    static class RefRule extends Rule{
        int number;
        Map<Integer, Rule> allRules;

        public RefRule(int number, Map<Integer, Rule> allRules) {
            this.number = number;
            this.allRules = allRules;
        }

        @Override
        String regex() {
            return allRules.get(number).regex();
        }
    }

    static class CharRule extends Rule {
        char c;
        public CharRule(char c) {
            this.c = c;
        }

        @Override
        public String regex() {
            return String.valueOf(c);
        }
    }

    static class SequenceRule extends Rule {
        Rule first;
        Rule second;
        public SequenceRule(String[] split, Map<Integer, Rule> allRules, Map<Integer, String> base) {

        }

        @Override
        public String regex() {
            return first.regex() + second.regex();
        }
    }

    static class OrRule extends Rule {
        Rule rules;
        Rule altRules;

        public OrRule(Rule rules, Rule altRules) {
            this.rules = rules;
            this.altRules = altRules;
        }

        @Override
        public String regex() {
            return "(" + rules.regex() + "|" + altRules.regex() + ")";
        }
    }

    static class Rule8 extends Rule {
        List<Rule> rules;
        public void setRules(List<Integer> rules, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            this.rules = listRules(rules, allRules, base);
        }
        public Rule8(String[] split, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            setRules(listNumbers(split, 1, 2), allRules, base);
        }

        @Override
        public String regex() {
            return "(" + buildStringFromRules(rules) + ")+";
        }
    }

    static class Rule11 extends Rule {
        List<Rule> rules;
        public void setRules(List<Integer> rules, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            this.rules = listRules(rules, allRules, base);
        }
        public Rule11(String[] split, Map<Integer, Rule> allRules, Map<Integer, String> base) {
            setRules(listNumbers(split, 1, 3), allRules, base);
        }
        @Override
        public String regex() {
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
    }
}
