import java.util.*;
import java.util.stream.*;

public class WordFrequencyCounter {
    private final Map<String, Integer> wordFreqs = new HashMap<>();

    public WordFrequencyCounter(WordFrequencyFramework wfapp, DataStorage dataStorage) {
        dataStorage.registerForWordEvent(this::incrementCount);
        wfapp.registerForEndEvent(this::printFreqs);
    }

    public void incrementCount(String word) {
        wordFreqs.put(word, wordFreqs.getOrDefault(word, 0) + 1);
    }

    public void printFreqs() {
        wordFreqs.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(25)
                .forEach(entry ->
                        System.out.println(entry.getKey() + " - " + entry.getValue()));
    }
}
