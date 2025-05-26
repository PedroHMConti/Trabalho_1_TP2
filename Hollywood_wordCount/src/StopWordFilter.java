import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class StopWordFilter {
    List<String> stopWords = new ArrayList<>();
    StopWordFilter(WordFrequencyFramework wfapp){
        wfapp.registerForLoadEvent((Consumer<String>) this::load);
    }
    public void load(String pathToFile) {
        try {
            BufferedReader br =new BufferedReader(new FileReader(new File("C:\\Users\\pedro\\OneDrive\\Área de Trabalho\\projetos_java\\Hollywood_wordCount\\src\\Files\\stopWords.txt")));
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
