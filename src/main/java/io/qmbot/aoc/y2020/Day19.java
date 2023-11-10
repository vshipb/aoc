package io.qmbot.aoc.y2020;

import io.qmbot.aoc.Puzzle;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.regex.Pattern;

public class Day19 implements Puzzle {
    @Override
    public String part1(String input) {
        String[] parts = input.split(REGEX_EMPTY_LINE);
        String[] rules = parts[0].split(REGEX_NEW_LINE);
        Map<Integer, Rule> allRules = new HashMap<>();
        for (String rule : rules) {
            String[] r = rule.split(": ");
            allRules.put(Integer.valueOf(r[0]), parseRule(r[1], allRules));
        }
        String regex = allRules.get(0).regex();
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
        for (String rule : rules) {
            String[] r = rule.split(": ");
            allRules.put(Integer.valueOf(r[0]), parseRule(r[1], allRules));
        }
        allRules.put(8, new Rule8(new RefRule(42, allRules)));
        allRules.put(11, new Rule11(new RefRule(42, allRules), new RefRule(31, allRules)));
        String regex = allRules.get(0).regex();
        int result = 0;
        for (String s : parts[1].split(REGEX_NEW_LINE)) {
            if (Pattern.matches(regex, s)) {
                result++;
            }
        }
        return String.valueOf(result);
    }

    static Rule parseRule(String str, Map<Integer, Rule> allRules) {
        if (str.contains("|")) {
            String[] split = str.split("\\|", 2);
            return new OrRule(parseRule(split[0].trim(), allRules), parseRule(split[1].trim(), allRules));
        } else if (str.contains(" ")) {
            String[] split = str.split(" ", 2);
            return new SequenceRule(parseRule(split[0].trim(), allRules), parseRule(split[1].trim(), allRules));
        } else if (str.contains("\"")) {
            return new CharRule(str.charAt(1));
        } else {
            return new RefRule(Integer.parseInt(str), allRules);
        }
    }

    private abstract static class Rule {

        abstract String regex();
    }

    static class RefRule extends Rule {
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
        char ch;

        public CharRule(char ch) {
            this.ch = ch;
        }

        @Override
        public String regex() {
            return String.valueOf(ch);
        }
    }

    static class SequenceRule extends Rule {
        Rule first;
        Rule second;

        public SequenceRule(Rule first, Rule second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String regex() {
            return first.regex() + second.regex() ;
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
        Rule rule;

        public Rule8(Rule rule) {
            this.rule = rule;
        }

        @Override
        public String regex() {
            return "(" + rule.regex() + ")+";
        }
    }

    static class Rule11 extends Rule {
        Rule firstRule;
        Rule secondRule;

        public Rule11(Rule firstRule, Rule secondRule) {
            this.firstRule = firstRule;
            this.secondRule = secondRule;
        }

        @Override
        public String regex() {
            StringJoiner str = new StringJoiner("|");
            for (int i = 1; i < 10; i++) {
                str.add("(" + firstRule.regex() + "{" + i + "}" + secondRule.regex() + "{" + i + "})");
            }
            return "(" + str + ")";
        }
    }
}
