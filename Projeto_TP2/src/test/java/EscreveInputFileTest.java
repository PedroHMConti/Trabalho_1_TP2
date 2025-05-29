import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class EscreveInputFileTest {

    @Test
    void testEscreveNoArquivoInput() throws Exception {
        // Usa um arquivo temporário para testar a escrita no arquivo
        Path tempFile = Files.createTempFile("Input", ".txt");

        // Instacia o objeto EscreveInputFile com o arquivo temporário
        EscreveInputFile escreveInputFile = new EscreveInputFile() {
            { file = tempFile.toFile(); }
        };

        // Escreve a linha de teste no arquivo
        String testLine = "The quick brown fox jumps over the lazy dog 1234567890";
        escreveInputFile.escreveNoArquivoInput(testLine);

        // Verifica se a escrita foi feita corretamente
        String content = Files.readString(tempFile).trim();
        assertEquals(testLine, content);

        Files.deleteIfExists(tempFile);
    }
}