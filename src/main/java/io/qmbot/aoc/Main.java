package io.qmbot.aoc;

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
        String dir = args[0];
        int year = Integer.parseInt(args[1]);
        String cookie = args[2];
        if (args.length < 4) {
            for (int i = 1; i < 26; i++) {
                doDay(dir, year, i, cookie);
            }
        } else {
            int day = Integer.parseInt(args[3]);
            doDay(dir, year, day, cookie);
        }
    }

    private static void doDay(String dir, int year, int day, String cookie) throws Exception {
        String formatted = String.format("io.qmbot.aoc.y%d.Day%02d", year, day);
        Puzzle p = (Puzzle) Class.forName(formatted).getDeclaredConstructor().newInstance();
        String input = getInput(dir, year, day, cookie);
        System.out.printf("Day %d answer%n%s%n%s%n%n", day, p.part1(input), p.part2(input));
    }

    private static String getInput(String dir, int year, int day, String cookie) throws IOException, InterruptedException {
        Path path = Path.of(String.format("%s/%d/%d.txt", dir, year, day));
        return (Files.exists(path)) ? Files.readString(path) : fetchInput(path, year, day, cookie);
    }

    private static String fetchInput(Path path, int year, int day, String cookie) throws IOException, InterruptedException {
        String url = String.format("https://adventofcode.com/%d/day/%d/input", year, day);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Cookie", cookie).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != HttpURLConnection.HTTP_OK) {
            throw new IllegalArgumentException(String.valueOf(response));
        }
        Files.createDirectories(path.getParent());
        String input = response.body();
        Files.write(path, Objects.requireNonNull(input).getBytes());
        return input;
    }
}
