package util;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.*;

public class TextProcessor {
    public static List<String> splitText(String text) {
        List<String> rawResult = Arrays.asList(text.split("[ \t\n]+"));
        List<String> processedResult = new ArrayList<>();
        for(String word: rawResult) {
            if (word.matches(".*\\d+.*")) continue;
            processedResult.add(word.trim().replaceAll("[.:\"()?,;]+", ""));
        }

        return processedResult;
    }

    public static List<String> splitTextByChapter(String text) {
        List<String> rawTextByChapter = new ArrayList<>(Arrays.asList(text.split("Chapter [0-9]+\n")));
        //we don't need text before chapter 1
        rawTextByChapter.remove(0);

        List<String> textByChapter = new ArrayList<>();
        for(String chapText: rawTextByChapter) {
            textByChapter.add(chapText.replaceAll("[\n]+", " "));
        }
        return textByChapter;
    }

    public static List<String> splitTextBySent(String text) {
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
        iterator.setText(text);
        int start = iterator.first();
        List<String> sentences = new ArrayList<>();
        for (int end = iterator.next();
             end != BreakIterator.DONE;
             start = end, end = iterator.next()) {
             sentences.add(text.substring(start,end));
        }

        return sentences;
    }

    public static Map<String, Integer> countWords(List<String> words) {
        return countWords(words, new HashSet<>());
    }

    public static Map<String, Integer> countWordsWithFilters(List<String> words, Set<String> filters) {
        return countWords(words, filters);
    }

    private static Map<String, Integer> countWords(List<String> words, Set<String> filters) {
        Map<String, Integer> wordCount = new HashMap<>();
        for(String word: words) {
            if(filters.contains(word)) continue;
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }
        return wordCount;
    }

    public static int countWord(List<String> words, String target) {
        int count = 0;
        for(String word: words) {
            if(word.equalsIgnoreCase(target))
                count++;
        }
        return count;
    }

    public static Map<String, List<String>> getWordsAfterDict(List<String> words) {
        Map<String, List<String>> wordsAfterDict = new HashMap<>();
        for(int i=1; i<words.size(); i++) {
            if(!wordsAfterDict.containsKey(words.get(i-1)))
               wordsAfterDict.put(words.get(i-1), new ArrayList<>());
            wordsAfterDict.get(words.get(i-1)).add(words.get(i));
        }
        return wordsAfterDict;
    }

    public static String randSelectWordFromList(List<String> list) {
        Random rand = new Random();
        int  n = rand.nextInt(list.size());
        return list.get(n);
    }

    public static String selectMostFreqWordFromList(Map<String, Integer> wordCount, List<String> list) {
        int maxFreq = Integer.MIN_VALUE;
        String mostFreqWord = "";

        for(String word: list) {
            if(wordCount.containsKey(word) && wordCount.get(word)>maxFreq) {
                mostFreqWord = word;
                maxFreq = wordCount.get(word);
            }
        }

        return mostFreqWord;
    }

}
