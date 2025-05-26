import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class DataStorage {
    String data = "";
    StopWordFilter stopWordFilter;
    List<Consumer<String>> wordEventsHandlers = new ArrayList<>();

    public DataStorage(WordFrequencyFramework wfapp, StopWordFilter stopWordFilter) {
        this.stopWordFilter = stopWordFilter;
        wfapp.registerForLoadEvent(this::load);
        wfapp.registerForDoworkEvent(this::produceWords);
    }

    // Carrega o conteúdo do arquivo e limpa os caracteres não alfabéticos
    public void load(String pathToFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(pathToFile)))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                data += linha.replaceAll("[\\W_]+", " ").toLowerCase();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Produz as palavras (excluindo as stop words) e dispara os eventos
    public void produceWords() {
        List<String> dataList = new ArrayList<>(Arrays.asList(data.split(" ")));
        for (String word : dataList) {
            if (!stopWordFilter.isStopWord(word)) {
                for (Consumer<String> c : wordEventsHandlers) {
                    c.accept(word);
                }
            }
        }
    }

    // Permite que outras classes registrem funções para receber as palavras
    public void registerForWordEvent(Consumer<String> handler) {
        wordEventsHandlers.add(handler);
    }
}
