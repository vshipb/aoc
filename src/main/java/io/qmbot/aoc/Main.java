package io.qmbot.aoc;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws Exception {
        final String cookie = args[0];
        final String name = args[1];
        final int year = Integer.parseInt(args[2]);
        if (args.length < 4) {
            for (int i = 1; i < 26; i++) {
                doDay(i, year, name, cookie);
            }
        } else {
            final int DAY = Integer.parseInt(args[3]);
            doDay(DAY, year, name, cookie);
        }
    }

    private static void doDay(int day, int year, String name, String folder) throws Exception {
        String formatted = String.format("io.qmbot.aoc.y%d.Day%02d", year, day);
        Puzzle p = (Puzzle) Class.forName(formatted).getDeclaredConstructor().newInstance();
        File file = new File(String.format("src/main/resources/%s/%d/%d.txt", name, year, day));
        if (!file.exists()) {
            writeFile(day, year, name, folder);
        }
        String input = Files.readString(Path.of(file.getPath()));
        System.out.printf("Day %d answer%n%s%n%s%n%n", day, p.part1(input), p.part2(input));

    }

    private static void writeFile(int day, int year, String name, String cookie) throws IOException, InterruptedException {
        String url = String.format("https://adventofcode.com/%d/day/%d/input", year, day);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie", cookie).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new IllegalArgumentException(String.valueOf(response));
        }
        String folder = String.format("src/main/resources/%s/%d", name, year);
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        Files.write(Path.of(folder, day + ".txt"), Objects.requireNonNull(response.body()).getBytes());
    }
}
