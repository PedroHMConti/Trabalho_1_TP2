import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    void testMainWithValidInput() throws Exception {
        File tempFile = File.createTempFile("input_test", ".txt");
        tempFile.deleteOnExit();

        String simulatedInput = String.join("\n",
                "2",
                "test line",
                "fim"
        );

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            EscreveInputFile testWriter = new EscreveInputFile(tempFile);
            Main.main(new String[]{}, testWriter);

            String output = outContent.toString();
            assertTrue(output.contains("Digite o tamanho da janela"));
            assertTrue(output.contains("Digite os contextos"));

            String fileContent = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
            assertTrue(fileContent.contains("test line"));

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testMainWithNegativeNumberThenValidInput() throws Exception {
        File tempFile = File.createTempFile("input_test", ".txt");
        tempFile.deleteOnExit();

        String simulatedInput = String.join("\n",
                "-1", "2",
                "context 1", "fim"
        );

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            EscreveInputFile testWriter = new EscreveInputFile(tempFile);
            Main.main(new String[]{}, testWriter);

            String output = outContent.toString();
            assertTrue(output.contains("janela deve ser um numero >= 0"));

            String fileContent = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
            assertTrue(fileContent.contains("context 1"));

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testMainWithInvalidNumberInput() throws Exception {
        File tempFile = File.createTempFile("input_test", ".txt");
        tempFile.deleteOnExit();

        String simulatedInput = String.join("\n",
                "abc", "2",
                "test", "fim"
        );

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
            ByteArrayOutputStream outContent = new ByteArrayOutputStream();
            System.setOut(new PrintStream(outContent));

            EscreveInputFile testWriter = new EscreveInputFile(tempFile);
            Main.main(new String[]{}, testWriter);

            String output = outContent.toString();
            assertTrue(output.contains("janela deve ser um numero >= 0"));

            String fileContent = new String(java.nio.file.Files.readAllBytes(tempFile.toPath()));
            assertTrue(fileContent.contains("test"));

        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
