import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import utils.FilePaths;

public class StopWordFilter {
    List<String> stopWords = new ArrayList<>();
    StopWordFilter(WordFrequencyFramework wfapp){
        wfapp.registerForLoadEvent((Consumer<String>) this::load);
    }
    public void load(String pathToFile) {
        try {
            BufferedReader br =new BufferedReader(new FileReader(new File(FilePaths.INPUT_FILE)));
            String linha;
            while((linha = br.readLine()) != null){
                stopWords.addAll(Arrays.stream(linha.split(",")).toList());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean isStopWord(String word){
        return stopWords.contains(word);
    }
}
