import util.Reader;
import util.TrieTree;

import java.io.IOException;
import java.util.*;

public class TextAnalyzer {
    private static final String TEXT_PATH = "../1342.txt";

    public static long getTotalNumberOfWords(String text) {
        return util.TextProcessor.splitText(text).size();
    }

    public static int getTotalUniqueWords(String text){
        List<String> words = util.TextProcessor.splitText(text);
        Set<String> uniqueWords = new HashSet<>(words);
        return uniqueWords.size();
    }

    public static List<WordCountPair> get20MostFrequentWords(String text) {
        List<String> words = util.TextProcessor.splitText(text);
        return get20MostOrLeastFrequentWordsWithFilters(words, new HashSet<>(), true);
    }

    public static List<WordCountPair> get20MostInterestingFrequentWords(String text, int topNCommonEngWordsToFilter) throws IOException {
        final String COMMON_ENG_WORDS_PATH = "./analyzer/resources/1000_common_english_words.txt";
        Reader reader = new Reader(COMMON_ENG_WORDS_PATH);
        List<String> commonEnglishWords = util.TextProcessor.splitText(reader.getText());

        Set<String> filterWords = new HashSet<>();
        for(int i=0; i<topNCommonEngWordsToFilter; i++) {
            filterWords.add(commonEnglishWords.get(i));
        }

        List<String> words = util.TextProcessor.splitText(text);
        return get20MostOrLeastFrequentWordsWithFilters(words, filterWords, true);
    }

    public static List<WordCountPair> get20LeastFrequentWords(String text) {
        List<String> words = util.TextProcessor.splitText(text);
        return get20MostOrLeastFrequentWordsWithFilters(words, new HashSet<>(), false);
    }

    public static List<Integer> getFrequencyOfWord(String text, String word) {
        List<String> textByChapter = util.TextProcessor.splitTextByChapter(text);
        List<Integer> result = new ArrayList<>();

        for(String chapText: textByChapter) {
            int wordChapCount = util.TextProcessor.countWord(util.TextProcessor.splitText(chapText), word);
            result.add(wordChapCount);
        }

        return result;
    }

    public static int getChapterQuoteAppears(String text, String quote) {
        List<String> textByChapter = util.TextProcessor.splitTextByChapter(text);
        for(int i=0; i<textByChapter.size(); i++) {
            if(textByChapter.get(i).contains(quote)) {
                return i+1;
            }
        }
        return -1;
    }

    public static String generateSentence(String text, String startWord, int len, boolean freqBased) throws IOException {
        StringBuilder sent = new StringBuilder();

        final String COMMON_ENG_WORDS_PATH = "./analyzer/resources/1000_common_english_words.txt";
        Reader reader = new Reader(COMMON_ENG_WORDS_PATH);
        List<String> commonEnglishWords = util.TextProcessor.splitText(reader.getText());
        Set<String> filterWords = new HashSet<>(commonEnglishWords);

        List<String> words = util.TextProcessor.splitText(text);
        Map<String, Integer> wordCount = util.TextProcessor.countWordsWithFilters(words, filterWords);
        Map<String, List<String>> wordsAfterDict = util.TextProcessor.getWordsAfterDict(words);

        for(int i=0; i<len; i++) {
            sent.append(startWord).append(" ");
            List<String> wordsAfterList = wordsAfterDict.get(startWord);
            if(freqBased) {
                startWord = util.TextProcessor.selectMostFreqWordFromList(wordCount, wordsAfterList);
            } else {
                startWord = util.TextProcessor.randSelectWordFromList(wordsAfterList);
            }
        }

        return sent.toString();
    }

    public static List<String> getAutocompleteSentence(String text, String startOfSentence) {
        TrieTree trie = new TrieTree(util.TextProcessor.splitTextBySent(text));
        return trie.query(startOfSentence);
    }

    //helper functions
    private static List<WordCountPair> get20MostOrLeastFrequentWordsWithFilters(List<String> words, Set<String> filters, boolean mostFrequent) {
        Map<String, Integer> wordCount = util.TextProcessor.countWords(words);

        List<WordCountPair> result = new ArrayList<>();
        Queue<Map.Entry<String, Integer>> pq;
        if (mostFrequent)
            pq = new PriorityQueue<>((a,b) -> b.getValue() - a.getValue());
        else
            pq = new PriorityQueue<>((a,b) -> a.getValue() - b.getValue());
        pq.addAll(wordCount.entrySet());
        int i=0;
        while(i<20) {
            Map.Entry<String, Integer> entry = pq.poll();
            if(filters.contains(entry.getKey()) || filters.contains(entry.getKey().toLowerCase())) continue;
            result.add(new WordCountPair(entry.getKey(), entry.getValue()));
            i++;
        }

        return result;
    }

    public static void main(String[] args) throws IOException{
        Reader reader = new Reader(TEXT_PATH);
        String text = reader.getText();

        System.out.println("Total number of words: " + getTotalNumberOfWords(text));
        System.out.println();

        System.out.println("Total number of unique words: " + getTotalUniqueWords(text));
        System.out.println();

        System.out.println("Most frequent 20 words:\n" + get20MostFrequentWords(text));
        System.out.println();
        System.out.println("Most frequent 20 interesting words with 100 common words filtered:\n" +
                get20MostInterestingFrequentWords(text, 100));
        System.out.println();
        System.out.println("Most frequent 20 interesting words with 200 common words filtered:\n" +
                get20MostInterestingFrequentWords(text, 200));
        System.out.println();
        System.out.println("Most frequent 20 interesting words with 300 common words filtered:\n" +
                get20MostInterestingFrequentWords(text, 300));
        System.out.println();
        System.out.println("Most frequent 20 interesting words with 1009 common words filtered:\n" +
                get20MostInterestingFrequentWords(text, 1009));
        System.out.println();

        System.out.println("Least frequent 20 words:\n" + get20LeastFrequentWords(text));
        System.out.println();

        System.out.println("Get frequency of word by chapter:\n" + getFrequencyOfWord(text, "Darcy"));
        System.out.println();

        System.out.println("Get chapter that the quote appears:\n" +
                getChapterQuoteAppears(text, "A lady's imagination is very rapid; it jumps from admiration to love, from love to matrimony, in a moment."));
        System.out.println();

        System.out.println("Generate 5 sentences:\n");
        for(int i=0; i<5; i++) {
            System.out.println(generateSentence(text, "The", 20, false));
            System.out.println();
        }
        System.out.println();

        System.out.println("Get auto complete sentences:\n");
        List<String> autoCompletedSent = getAutocompleteSentence(text, "She is the");
        for(String sent: autoCompletedSent) {
            System.out.println(sent.trim());
            System.out.println();
        }



    }
}

class WordCountPair {
    protected String word;
    protected int count;

    public WordCountPair(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String toString() {
        return "[" + this.word + ", " + this.count + "]";
    }
}

