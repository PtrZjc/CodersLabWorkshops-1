package MostPopularWords;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class temp {

    public static void main(String[] args) {

        String cssSelector = "span[class]";
//        String cssSelector = "a[tile-magazine-title-url]";

//        String url = "https://www.tvn24.pl/";
        String url = "http://www.gazeta.pl/";

        Connection connect = Jsoup.connect(url);
        Set<String> wordSet = new HashSet<>();

        try {
            Document document = connect.get();
            Elements titles = document.select(cssSelector);
            for (Element title : titles) {
                System.out.println(title.text());
                for (String word : title.text().split("[!()-:\\[\\]?„”\\\",.\\s]")) {
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
    }

}

