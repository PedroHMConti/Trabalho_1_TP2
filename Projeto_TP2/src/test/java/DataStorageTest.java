
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataStorageTest {

    @Test
    void testLoad() throws IOException {
        // Cria arquivo temporário com conteúdo de teste
        Path tempFile = Files.createTempFile("testInput", ".txt");
        Files.writeString(tempFile, "The quick brown fox\nA brown cat sat");

        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorage = new DataStorage(wfapp, stopWordFilter, 3);

        // Chama o metodo load diretamente
        dataStorage.load(tempFile.toString());

        // Verifica se as linhas foram carregadas e limpas corretamente
        assertEquals(2, dataStorage.linhas.size());
        assertEquals("the quick brown fox", dataStorage.linhas.get(0));
        assertEquals("a brown cat sat", dataStorage.linhas.get(1));

        // Remove o arquivo temporário
        Files.delete(tempFile);
    }


    @Test
    void testProduceKWICContext() {
        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorage = new DataStorage(wfapp, stopWordFilter, 3);

        // Adiciona linhas de teste diretamente
        dataStorage.linhas.add("the quick brown fox");
        dataStorage.linhas.add("a brown cat sat");

        // Lista para capturar os contextos gerados
        List<String> resultados = new ArrayList<>();
        dataStorage.registerForWordEvent(resultados::add);

        // Chama o metodo para produzir o contexto KWIC
        dataStorage.produceKWICContext();

        // Verifica se os contextos KWIC foram produzidos corretamente
        assertTrue(resultados.contains("quick brown fox the (from: the quick brown fox)"));
        assertTrue(resultados.contains("brown fox the quick (from: the quick brown fox)"));
        assertTrue(resultados.contains("fox the quick brown (from: the quick brown fox)"));
        assertTrue(resultados.contains("brown cat sat a (from: a brown cat sat)"));
        assertTrue(resultados.contains("cat sat a brown (from: a brown cat sat)"));
        assertTrue(resultados.contains("sat a brown cat (from: a brown cat sat)"));
    }
}
