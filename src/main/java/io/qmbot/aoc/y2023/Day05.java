package io.qmbot.aoc.y2023;

import io.qmbot.aoc.Puzzle;

import java.util.*;

public class Day05 implements Puzzle {
    @Override
    public Long part1(String input) {
        Card card = parse(input);
        List<Long> result = new ArrayList<>();
        for (long seed : card.seeds) {
            seed = processMap(card.seedToSoil, seed);
            seed = processMap(card.soilToFertilizer, seed);
            seed = processMap(card.fertilizerToWater, seed);
            seed = processMap(card.waterToLight, seed);
            seed = processMap(card.lightToTemperature, seed);
            seed = processMap(card.temperatureToHumidity, seed);
            seed = processMap(card.humidityToLocation, seed);
            result.add(seed);
        }
        return result.stream().min(Long::compareTo).orElseThrow();
    }

    @Override
    public Object part2(String input) {
        Card card = parse(input);
        List<Long> result = new ArrayList<>();
        long start = card.seeds.get(0);
        long length = card.seeds.get(1);
        for (long i = start; i < start + length; i++) {
            long seed  = i;
            seed = processMap(card.seedToSoil, seed);
            seed = processMap(card.soilToFertilizer, seed);
            seed = processMap(card.fertilizerToWater, seed);
            seed = processMap(card.waterToLight, seed);
            seed = processMap(card.lightToTemperature, seed);
            seed = processMap(card.temperatureToHumidity, seed);
            seed = processMap(card.humidityToLocation, seed);
            result.add(seed);
        }
        start = card.seeds.get(2);
        length = card.seeds.get(3);
        for (long i = start; i < start + length; i++) {
            long seed  = i;
            seed = processMap(card.seedToSoil, seed);
            seed = processMap(card.soilToFertilizer, seed);
            seed = processMap(card.fertilizerToWater, seed);
            seed = processMap(card.waterToLight, seed);
            seed = processMap(card.lightToTemperature, seed);
            seed = processMap(card.temperatureToHumidity, seed);
            seed = processMap(card.humidityToLocation, seed);
            result.add(seed);
        }
        return result.stream().min(Long::compareTo).orElseThrow();
    }

    private long processMap(List<List<Long>> map, long seed) {
        Set<Long> numbers = new HashSet<>();
        numbers.add(seed);
        long n = seed;
        for (List<Long> list : map) {
            long value = value(list, seed);
            if (numbers.add(value)) {
                n = value;
            }
        }
        return n;
    }


    private long value(List<Long> list, long seed) {
        Long source = list.get(1);
        Long length = list.get(2);
        if (source <= seed && seed <= source + length -1) {
            return list.get(0) + (seed - source);
        }
        return seed;
    }

    private Card parse(String input) {
        List<List<Long>>[] maps = new List[7];
        String[] strings = input.split(REGEX_NEW_LINE);
        String[] seedsStrings = strings[0].split(" ");
        int mapIndex = 0;
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]) {
                case "seed-to-soil map:", "soil-to-fertilizer map:", "fertilizer-to-water map:", "water-to-light map:",
                        "light-to-temperature map:", "temperature-to-humidity map:", "humidity-to-location map:" ->
                        maps[mapIndex++] = list(strings, i);
            }
        }
        return new Card(Arrays.stream(seedsStrings, 1, seedsStrings.length).map(Long::valueOf).toList(),
                maps[0], maps[1], maps[2], maps[3], maps[4], maps[5], maps[6]);
    }

    List<List<Long>> list(String[] strings, int i) {
        i++;
        List<List<Long>> list = new ArrayList<>();
        String[] values;
        while (i < strings.length && !strings[i].equals("")) {
            values = strings[i].split(" ");
            list.add(somethingToSomething(values[0], values[1], values[2]));
            i++;
        }
        return list;
    }

    static class Card {
        List<Long> seeds;
        List<List<Long>> seedToSoil;
        List<List<Long>> soilToFertilizer;
        List<List<Long>> fertilizerToWater;
        List<List<Long>> waterToLight;
        List<List<Long>> lightToTemperature;
        List<List<Long>> temperatureToHumidity;
        List<List<Long>> humidityToLocation;

        public Card(List<Long> seeds, List<List<Long>> seedToSoil, List<List<Long>> soilToFertilizer,
                    List<List<Long>> fertilizerToWater, List<List<Long>> waterToLight,
                    List<List<Long>> lightToTemperature, List<List<Long>> temperatureToHumidity,
                    List<List<Long>> humidityToLocation) {
            this.seeds = seeds;
            this.seedToSoil = seedToSoil;
            this.soilToFertilizer = soilToFertilizer;
            this.fertilizerToWater = fertilizerToWater;
            this.waterToLight = waterToLight;
            this.lightToTemperature = lightToTemperature;
            this.temperatureToHumidity = temperatureToHumidity;
            this.humidityToLocation = humidityToLocation;
        }
    }

    public List<Long> somethingToSomething(String destination, String source, String length) {
        List<Long> smtToSmt = new ArrayList<>();
        smtToSmt.add(Long.parseLong(destination));
        smtToSmt.add(Long.parseLong(source));
        smtToSmt.add(Long.parseLong(length));

        return smtToSmt;
    }

}
