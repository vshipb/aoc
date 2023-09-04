package io.qmbot.aoc.y2022;

import io.qmbot.aoc.Puzzle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day25 implements Puzzle {
    @Override
    public String part1(String input) {
        return Arrays.stream(input.split("\n")).map(SNAFU::parse).reduce(SNAFU::add).get().toString();
    }

    static class SNAFU {
        private SNAFU(List<Integer> digits) {
            this.digits = digits;
        }

        @Override
        public String toString() {
            char[] mapping = {'=', '-', '0', '1', '2'};
            StringBuilder stringBuilder = new StringBuilder(digits.size());
            for (int i = digits.size() - 1; i >= 0; i--) {
                int index = digits.get(i) + 2;
                stringBuilder.append(mapping[index]);
            }
            return stringBuilder.toString();
        }

        List<Integer> digits;

        static SNAFU parse(String string) {
            List<Integer> digits = string.chars().mapToObj(c -> switch (c) {
                case '=' -> -2;
                case '-' -> -1;
                case '0', '1', '2' -> Character.getNumericValue(c);
                default -> throw new IllegalArgumentException("Wrong char " + (char) c);
            }).collect(Collectors.toList());
            Collections.reverse(digits);
            return new SNAFU(digits);
        }

        SNAFU add(SNAFU snafu) {
            int max = Math.max(digits.size(), snafu.digits.size());
            List<Integer> sum = new ArrayList<>();
            int p = 0;
            for (int i = 0; i < max; i++) {
                int newInt = (digits.size() > i ? digits.get(i) : 0) + (snafu.digits.size() > i ? snafu.digits.get(i) : 0) + p;
                if (newInt > 2) {
                    sum.add(newInt - 5);
                    p = 1;
                } else if (newInt < -2) {
                    sum.add(newInt + 5);
                    p = -1;
                } else {
                    sum.add(newInt);
                    p = 0;
                }
            }
            if (p != 0) sum.add(p);
            return new SNAFU(sum);
        }
    }

    @Override
    public String part2(String input) {
        return null;
    }
}
