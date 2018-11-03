package MostPopularWords;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class main {
    public static void main(String[] args) {
        parseMultipleFromWeb("input.txt");

    }

//    static void findCommonWords(List<String>) {
//
//    }

    static void parseMultipleFromWeb(String inputFile) {
        Path path = Paths.get("src", "MostPopularWords", inputFile);
        try {
            List<String> inputList = Files.readAllLines(path);
            String[] inputData = new String[2];
            for (String inputLine : inputList) {
                inputData = inputLine.split(" ");
                parseFromWeb(inputData[0], inputData[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseFromWeb(String cssSelector, String url) {
        Connection connect = Jsoup.connect(url);
        Set<String> wordSet = new HashSet<>();
        List<String> fileNameList = new LinkedList<>();
        try {
            Document document = connect.get();
            Elements titles = document.select(cssSelector);
            for (Element title : titles) {
                for (String word : title.text().split("'[!()-:\\[\\]?„”\\\",.\\s]")) {
                    if (word.length() > 3 && !Character.isDigit(word.charAt(0))) {
                        wordSet.add(word.toLowerCase());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> words = new LinkedList<>();
        for (String word : wordSet) {
            words.add(word);
        }
        Collections.sort(words);
//        words.stream().forEach(System.out::println);
        String filename = url.split("\\.")[1];
        fileNameList.add(filename);
        saveListToFile(words, filename);
        saveListToFile(fileNameList, "fileNames");
    }

    public static void saveListToFile(List words, String filename) {
        Path path = Paths.get("src", "MostPopularWords", filename + ".txt");
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            Files.write(path, words);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
