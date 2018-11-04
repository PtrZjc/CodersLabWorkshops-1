package MostPopularWords;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class main {

    static HashSet<String> fileNameSet = new HashSet<>();

    public static void main(String[] args) {
        parseMultipleFromWeb("input.txt");
        findCommonWords();
    }

    static void findCommonWords() {
        try {
            Path filePath;
            HashMap<String, Integer> countedWords = new HashMap<>();
            for (String file : fileNameSet) {
                filePath = Paths.get("src", "MostPopularWords", "WordsLibrary", file + ".txt");
                for (String word : Files.readAllLines(filePath)) {
                    countedWords.putIfAbsent(word, 0);
                    countedWords.put(word, countedWords.get(word) + 1);
                }
            }
            compareAndSaveToFile(countedWords);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void parseMultipleFromWeb(String inputFile) {
        Path path = Paths.get("src", "MostPopularWords", inputFile);
        try {
            List<String> inputList = Files.readAllLines(path);
            String[] inputData;
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

        try {
            Document document = connect.get();
            Elements titles = document.select(cssSelector);
            for (Element title : titles) {
                for (String word : title.text().split("[^0-9a-zA-ZąęćółśźżŚŹŻŁĆ']+")) {
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
        String filename = url.split("\\.")[1];
        fileNameSet.add(filename);
        saveListToFile(words, filename, false);
    }

    public static void saveListToFile(List words, String filename, boolean append) {
        Path path = Paths.get("src", "MostPopularWords", "WordsLibrary", filename + ".txt");
        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
            }
            if (append) {
                Files.write(path, words, StandardOpenOption.APPEND);
            } else {
                Files.write(path, words);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void compareAndSaveToFile(HashMap map) {
        Path commonWords = Paths.get("src", "MostPopularWords", "CommonWords.txt");
        List<String> tempWords = new LinkedList<>();
        Iterator<Map.Entry<String, Integer>> iter;
        BufferedWriter writer = null;
        int counter = 0;

        try {
            if (!Files.exists(commonWords)) {
                Files.createFile(commonWords);
            }
            Files.write(commonWords, "".getBytes());
            writer = Files.newBufferedWriter(commonWords, StandardOpenOption.APPEND);

            writer.write("The commonly occurring words were analyzed on the following websites:\n\n");
            for (String file : fileNameSet) {
                writer.write(file + "\n");
            }

            //section responsible for comparing the words between the sites:
            for (int i = fileNameSet.size(); i > 1; i--) {
                writer.flush();
                if (map.containsValue(i)) {
                    writer.write("\n\nWords occurring in the quantity of " + i + ":\n\n");
                } else {
                    continue;
                }

                iter = map.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry<String, Integer> pair = iter.next();
                    if (pair.getValue() == i) {
                        tempWords.add(String.format("%-16s", pair.getKey()));
                    }
                }
                Collections.sort(tempWords);
                for (String word : tempWords) {
                    writer.write(word);
                    counter++;
                    if (counter == 3) {
                        writer.write("\n");
                        counter = 0;
                    }
                }
                System.out.println(tempWords.toString());
                tempWords.clear();
                counter = 0;
            }
        } catch (
                IOException e) {
            e.getStackTrace();
        }

    }
}
