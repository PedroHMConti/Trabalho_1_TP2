

import java.io.*;
import java.util.*;
import java.util.function.Consumer;

public class StopWordFilter {
    private final List<String> stopWords = new ArrayList<>();
    private final String pathToUse;

    public StopWordFilter(WordFrequencyFramework wfapp) {
        this(wfapp, null);
    }

    public StopWordFilter(WordFrequencyFramework wfapp, String customPath) {
        this.pathToUse = (customPath != null && !customPath.isEmpty())
                ? customPath
                : FilePaths.STOP_WORDS_FILE;

        wfapp.registerForLoadEvent((Consumer<String>) this::load);
    }

    private void load(String ignored) {
        try (BufferedReader br = new BufferedReader(new FileReader(this.pathToUse))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                stopWords.addAll(Arrays.asList(linha.split(",")));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar stop words de: " + this.pathToUse, e);
        }
    }

    public boolean isStopWord(String word) {
        return stopWords.contains(word);
    }
}
