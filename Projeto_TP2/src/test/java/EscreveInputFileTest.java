
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
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
        String testLine = "the quick brown fox jumps over the lazy dog 1234567890 THE QUICK BROWN FOX JUMPS OVER THE LAZY DOG";
        escreveInputFile.escreveNoArquivoInput(testLine);

        // Verifica se a escrita foi feita corretamente
        String content = Files.readString(tempFile).trim();
        assertEquals(testLine, content);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testEscreveNoArquivoInputThrowsIOException() throws IOException {
        File tempFile = File.createTempFile("readonly", ".txt");
        tempFile.setReadOnly();

        EscreveInputFile escreveInputFile = new EscreveInputFile() {{
            file = tempFile;
        }};

        assertDoesNotThrow(() -> escreveInputFile.escreveNoArquivoInput("teste"));

        tempFile.setWritable(true);
        tempFile.delete();
    }

    @Test
    void testEscreveArquivoInputVazio() throws Exception {
        // Usa um arquivo temporário para testar a escrita no arquivo
        Path tempFile = Files.createTempFile("Input", ".txt");

        // Instancia o objeto EscreveInputFile com o arquivo temporário
        EscreveInputFile escreveInputFile = new EscreveInputFile() {
            { file = tempFile.toFile(); }
        };

        // Escreve uma linha vazia no arquivo
        escreveInputFile.escreveNoArquivoInput("");

        // Verifica se a escrita foi feita corretamente
        String content = Files.readString(tempFile).trim();
        assertEquals("", content);

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testEscreveArquivoMultiplasLinhas() throws Exception {
        Path tempFile = Files.createTempFile("Input", ".txt");
        EscreveInputFile escreveInputFile = new EscreveInputFile() {{ file = tempFile.toFile(); }};
        escreveInputFile.escreveNoArquivoInput("linha1");
        escreveInputFile.escreveNoArquivoInput("linha2");
        String content = Files.readString(tempFile).trim();
        assertEquals("linha1\nlinha2", content.replace("\r", ""));
        Files.deleteIfExists(tempFile);
    }
}