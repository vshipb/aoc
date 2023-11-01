package io.qmbot.aoc.y2020;

import io.qmbot.aoc.Puzzle;

import java.util.*;
import java.util.stream.Collectors;

public class Day19 implements Puzzle {
    @Override
    public String part1(String input) {
        String[] parts = input.split(REGEX_EMPTY_LINE);
        String[] rules = parts[0].split(REGEX_NEW_LINE);
        Map<Integer, Rule> allRules = allRules(rules);

        List<String> ready = new ArrayList<>();
        ready.add("");
        trueStrings(ready, 0, allRules);
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
        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(rules));
        while (arrayList.contains("8: 42")) {
            arrayList.remove("8: 42");
            arrayList.add("8: 42 | 42 8");
        }
        while (arrayList.contains("11: 42 31")) {
            arrayList.remove("11: 42 31");
            arrayList.add("11: 42 31 | 42 11 31");
        }
        rules = arrayList.toArray(new String[0]);
        Map<Integer, Rule> allRules = allRules(rules);
        List<String> ready = new ArrayList<>();
        ready.add("");
        trueStrings(ready, 0, allRules);
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

    private static void trueStrings(List<String> ready, int number, Map<Integer, Rule> allRules) {
        Rule mainRule = allRules.get(number);
        if (mainRule.c != '\0') {
            ready.replaceAll(s -> s + mainRule.c);
        }
        if (mainRule.rules != null) {
            List<String> addReady = new ArrayList<>(ready);
            for (Rule rule : mainRule.rules) {
                trueStrings(addReady, rule.number, allRules);
            }
            List<String> addReady2 = new ArrayList<>(ready);
            if (mainRule.altRules != null) {
                for (Rule rule : mainRule.altRules) {
                    trueStrings(addReady2, rule.number, allRules);
                }
            }
            ready.clear();
            ready.addAll(addReady);
            ready.addAll(addReady2);
        }
    }

    private static Map<Integer, Rule> allRules(String[] rules) {
        Map<Integer, Rule> allRules = new HashMap<>();
        for (String rule : rules) {
            String[] split = rule.split(" ");
            int number = Integer.parseInt(split[0].substring(0, split[0].length() - 1));
            Rule currentRule = allRules.computeIfAbsent(number, Rule::new);
            if (split[1].contains("\"")) {
                currentRule.setC(split[1].charAt(1));
            } else {
                int index = Arrays.asList(split).indexOf("|");
                currentRule.setRules(listRules(split, 1, index > 0 ? index : split.length), allRules);
                currentRule.setAltRules(index > 0 ? listRules(split, index + 1, split.length) : new ArrayList<>(), allRules);
            }
        }
        return allRules;
    }

    private static List<Integer> listRules (String[] split, int start, int end){
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

        public void setRules(List<Integer> rules, Map<Integer, Rule> allRules) {
            this.rules = listRules(rules, allRules);
        }

        public void setAltRules(List<Integer> rules, Map<Integer, Rule> allRules) {
            this.altRules = listRules(rules, allRules);
        }

        private static List<Rule> listRules(List<Integer> rules, Map<Integer, Rule> allRules) {
            return rules.stream().map(rule -> allRules.containsKey(rule) ? allRules.get(rule) : new Rule(rule))
                    .peek(newRule -> allRules.put(newRule.number, newRule)).toList();
        }


        public char getC() {
            return c;
        }

        public void setC(char c) {
            this.c = c;
        }
    }
}
