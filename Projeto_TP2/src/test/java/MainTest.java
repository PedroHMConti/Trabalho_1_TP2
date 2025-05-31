import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    @Test
    void testMainWithValidInput() throws Exception {
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

            Main.main(new String[]{});

            String output = outContent.toString();
            assertTrue(output.contains("Digite o tamanho da janela"));
            assertTrue(output.contains("Digite os contextos"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testMainWithNegativeNumberThenValidInput() throws Exception {
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

            Main.main(new String[]{});

            String output = outContent.toString();
            assertTrue(output.contains("janela deve ser um numero >= 0"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }

    @Test
    void testMainWithInvalidNumberInput() throws Exception {
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

            Main.main(new String[]{});

            String output = outContent.toString();
            assertTrue(output.contains("janela deve ser um numero >= 0"));
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}
