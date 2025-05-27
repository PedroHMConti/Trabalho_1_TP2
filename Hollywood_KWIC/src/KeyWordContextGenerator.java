import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KeyWordContextGenerator {
    private final List<String> kwicLines = new ArrayList<>();

    public KeyWordContextGenerator(WordFrequencyFramework wfapp, DataStorage dataStorage) {
        // Registrar para receber as linhas rotacionadas com contexto
        dataStorage.registerForWordEvent(this::storeLine);
        // Registrar para evento de fim, para imprimir a lista ordenada
        wfapp.registerForEndEvent(this::printKWIC);
    }

    private void storeLine(String line) {
        kwicLines.add(line);
    }

    private void printKWIC() {
        // Ordena alfabeticamente a lista de linhas rotacionadas
        Collections.sort(kwicLines);

        // Imprime cada linha
        for (String line : kwicLines) {
            System.out.println(line);
        }
    }
}
