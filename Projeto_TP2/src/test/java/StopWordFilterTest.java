import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

class StopWordFilterTest {
    @Test
    void testIsStopWordInstrucao() {
        // Cria um objeto WordFrequencyFramework para poder instanciar o StopWordFilter
        WordFrequencyFramework wfapp = new WordFrequencyFramework();

        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        // Adiciona palavras de parada manualmente para o teste
        stopWordFilter.stopWords.addAll(Arrays.asList("the", "and", "a"));

        assertTrue(stopWordFilter.isStopWord("the"));
        assertTrue(stopWordFilter.isStopWord("and"));
        assertFalse(stopWordFilter.isStopWord("cat"));
        assertFalse(stopWordFilter.isStopWord("THE")); // Testa case insensitivity
    }

    @Test
    void testIsStopWordVazio() {
        // Cria um objeto WordFrequencyFramework para poder instanciar o StopWordFilter
        WordFrequencyFramework wfapp = new WordFrequencyFramework();

        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);

        // Esvazia a lista de palavras de parada para testar o funcionamento quando não há palavras de parada
        stopWordFilter.stopWords.clear();

        assertFalse(stopWordFilter.isStopWord("the"));
        assertFalse(stopWordFilter.isStopWord("and"));
        assertFalse(stopWordFilter.isStopWord("cat"));
    }

}