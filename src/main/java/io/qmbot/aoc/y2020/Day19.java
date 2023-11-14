package io.qmbot.aoc.y2020;

import io.qmbot.aoc.Puzzle;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day19 implements Puzzle {
    @Override
    public Integer part1(String input) {
        String[] parts = input.split(REGEX_EMPTY_LINE);
        return (int) Arrays.stream(parts[1].split(REGEX_NEW_LINE))
                .filter(s -> Pattern.matches(allRules(parts[0]).get(0).regex(), s)).count();
    }

    @Override
    public Integer part2(String input) {
        String[] parts = input.split(REGEX_EMPTY_LINE);
        Map<Integer, Rule> allRules = allRules(parts[0]);
        allRules.put(8, new Rule8(new RefRule(42, allRules)));
        allRules.put(11, new Rule11(new RefRule(42, allRules), new RefRule(31, allRules)));
        return (int) Arrays.stream(parts[1].split(REGEX_NEW_LINE))
                .filter(s -> Pattern.matches(allRules.get(0).regex(), s)).count();
    }

    static Map<Integer, Rule> allRules(String blueprint) {
        Map<Integer, Rule> allRules = new HashMap<>();
        for (String rule : blueprint.split(REGEX_NEW_LINE)) {
            String[] r = rule.split(": ");
            allRules.put(Integer.valueOf(r[0]), parseRule(r[1], allRules));
        }
        return allRules;
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
            return IntStream.range(1, 10)
                    .mapToObj(i -> String.format("(%s{%d}%s{%d})", firstRule.regex(), i, secondRule.regex(), i))
                    .collect(Collectors.joining("|", "(", ")"));
        }
    }
}
