package io.qmbot.aoc;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Main {
    public static void main(String[] args) throws Exception {
        final String COOKIE = args[0];
        final String NAME = args[1];
        final int YEAR = Integer.parseInt(args[2]);
        if (args.length < 4) {
            for (int i = 1; i < 26; i++){
                doDay(i, YEAR, NAME, COOKIE);
            }
        } else {
            final int DAY = Integer.parseInt(args[3]);
            doDay(DAY, YEAR, NAME, COOKIE);
        }
    }

    private static void doDay(int DAY, int YEAR, String NAME, String FOLDER) throws Exception {
        String formatted = String.format("io.qmbot.aoc.y%d.Day%02d", YEAR, DAY);
        Puzzle p = (Puzzle) Class.forName(formatted).getDeclaredConstructor().newInstance();
        File file = new File(String.format("src/main/resources/%s/%d/%d.txt", NAME, YEAR, DAY));
        if (!file.exists()) {
            writeFile(DAY, YEAR, NAME, FOLDER);
        }
        String input = Files.readString(Path.of(file.getPath()));
        System.out.printf("Day %d answer%n%s%n%s%n%n", DAY, p.part1(input), p.part2(input));

    }

    private static void writeFile(int DAY, int YEAR, String NAME, String COOKIE) throws IOException {
        String url = String.format("https://adventofcode.com/%d/day/%d/input", YEAR, DAY);
        Request request = new Request.Builder().url(url).addHeader("Cookie", COOKIE).build();
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IllegalArgumentException(String.valueOf(response));
        }
        String folder = String.format("src/main/resources/%s/%d", NAME, YEAR);
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        Files.write(Path.of(folder, DAY + ".txt"), Objects.requireNonNull(response.body()).string().getBytes());
    }
}
