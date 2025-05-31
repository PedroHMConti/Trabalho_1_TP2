import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.embed.swing.JFXPanel;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class KWICControllerTest {

    private KWICController controller;

    @BeforeAll
    static void initToolkit() throws Exception {
        new JFXPanel();
    }

    @BeforeEach
    void setup() {
        controller = new KWICController();

        controller.filePathField = new TextField();
        controller.inputTextArea = new TextArea();
        controller.stopWordsPathField = new TextField();
        controller.stopWordsTextArea = new TextArea();
        controller.outputTextArea = new TextArea();
        controller.contextWindowField = new TextField();
    }

    @Test
    void testSetFieldsWithValidFile() throws Exception {
        File tempFile = File.createTempFile("test", ".txt");
        Files.writeString(tempFile.toPath(), "linha de teste");

        controller.setFields(tempFile);

        assertEquals(tempFile.getAbsolutePath(), controller.filePathField.getText());
        assertEquals("linha de teste", controller.inputTextArea.getText());

        tempFile.delete();
    }

    @Test
    void testSetFieldsWithNullFile() {
        controller.filePathField.setText("não deve permanecer");
        controller.inputTextArea.setText("também não");

        controller.setFields(null);

        assertTrue(controller.filePathField.getText().isEmpty());
        assertTrue(controller.inputTextArea.getText().isEmpty());
    }

    @Test
    void TestSetFieldsThrowsException() {
        File tempFile = new File("arquivo_inexistente.txt");

        controller.setFields(tempFile);

        assertTrue(controller.inputTextArea.getText().contains("Erro ao ler o arquivo:"));
    }

    @Test
    void testGetPathReturnsEmptyStringWhenNoFileSelected() {
        KWICController.FileChooserProvider mockProvider = () -> null;
        String result = KWICController.getPath(mockProvider);
        assertEquals("", result);
    }

    @Test
    void testGetPathReturnsCorrectPathWhenFileSelected() {
        KWICController.FileChooserProvider mockProvider = () -> new File("/fake/path.txt");
        String result = KWICController.getPath(mockProvider);
        assertEquals("/fake/path.txt", result);
    }

    @Test
    void testHandleFileLoadWithValidPath() {
        KWICController controller = new KWICController();
        controller.filePathField = new TextField();
        controller.inputTextArea = new TextArea();

        String testPath = "/tmp/test.txt";

        controller.handleFileLoad(testPath);

        assertEquals(testPath, KWICApplication.textPath);
        assertEquals(testPath, controller.filePathField.getText());
    }

    @Test
    void testHandleStopWordsLoad_withValidFile() throws Exception {
        File temp = File.createTempFile("stopwords", ".txt");
        String conteudo = "the\nand\nor\nbut";
        Files.writeString(temp.toPath(), conteudo);

        KWICController controller = new KWICController();
        controller.stopWordsPathField = new TextField();
        controller.stopWordsTextArea = new TextArea();

        controller.handleStopWordsLoad(temp.getAbsolutePath());

        assertEquals(temp.getAbsolutePath(), controller.stopWordsPathField.getText());
        assertEquals(conteudo, controller.stopWordsTextArea.getText());
        assertEquals(temp.getAbsolutePath(), KWICApplication.stopWordPath);

        temp.deleteOnExit();
    }

    @Test
    void testHandleStopWordsLoad_withEmptyPath() {
        KWICController controller = new KWICController();
        controller.stopWordsPathField = new TextField("old path");
        controller.stopWordsTextArea = new TextArea("old content");

        controller.handleStopWordsLoad("");

        assertEquals("", controller.stopWordsPathField.getText());
        assertEquals("", controller.stopWordsTextArea.getText());
    }

    @Test
    void testLoadInitialFiles_withExistingFiles() throws Exception {
        File inputFile = File.createTempFile("input", ".txt");
        File stopFile = File.createTempFile("stop", ".txt");

        String inputContent = "some input content";
        String stopWordsContent = "a\nthe\nin";

        Files.writeString(inputFile.toPath(), inputContent);
        Files.writeString(stopFile.toPath(), stopWordsContent);

        KWICController controller = new KWICController();
        controller.inputTextArea = new TextArea();
        controller.stopWordsTextArea = new TextArea();
        controller.filePathField = new TextField();
        controller.stopWordsPathField = new TextField();

        controller.loadInitialFiles(inputFile.getAbsolutePath(), stopFile.getAbsolutePath());

        assertEquals(inputContent, controller.inputTextArea.getText());
        assertEquals(stopWordsContent, controller.stopWordsTextArea.getText());
        assertEquals(stopFile.getAbsolutePath(), controller.stopWordsPathField.getText());
        assertEquals(stopFile.getAbsolutePath(), KWICApplication.stopWordPath);
        assertEquals(inputFile.getAbsolutePath(), KWICApplication.textPath);

        inputFile.deleteOnExit();
        stopFile.deleteOnExit();
    }

    @Test
    void testLoadInitialFiles_handlesExceptionGracefully() {
        KWICController controller = new KWICController();
        controller.inputTextArea = new TextArea();
        controller.stopWordsTextArea = new TextArea();
        controller.filePathField = new TextField();
        controller.stopWordsPathField = new TextField();

        controller.loadInitialFiles("caminho/que/nao/existe.txt", ".");

        String erro = controller.inputTextArea.getText();
        assertTrue(
                erro.startsWith("Erro ao carregar arquivos padrão:"));
    }

    @Test
    void testOnRunButtonClick_withoutInputPath_setsErrorMessage() {
        KWICController controller = new KWICController();

        controller.outputTextArea = new TextArea();
        controller.contextWindowField = new TextField("1");

        KWICApplication.textPath = "";
        KWICApplication.stopWordPath = "dummy";

        controller.onRunButtonClick();

        String output = controller.outputTextArea.getText();
        assertEquals("Erro: Nenhum arquivo de input carregado.", output);
    }

    @Test
    void testOnRunButtonClick_withoutStopWordPath_setsErrorMessage() {
        KWICController controller = new KWICController();

        controller.outputTextArea = new TextArea();
        controller.contextWindowField = new TextField("1");

        KWICApplication.textPath = "dummy";
        KWICApplication.stopWordPath = "";

        controller.onRunButtonClick();

        String output = controller.outputTextArea.getText();
        assertEquals("Erro: Nenhum arquivo de stop words carregado.", output);
    }

    @Test
    void testOnRunButtonClick_invalidContextWindow_setsErrorMessage() {
        KWICController controller = new KWICController();

        controller.outputTextArea = new TextArea();
        controller.contextWindowField = new TextField("abc");

        KWICApplication.textPath = "input.txt";
        KWICApplication.stopWordPath = "stopwords.txt";

        controller.onRunButtonClick();

        String output = controller.outputTextArea.getText();
        assertEquals("Por favor, insira um número válido (>= 0) para o tamanho da janela de contexto.", output);
    }

    @Test
    void testOnRunButtonClick_negativeContextSizeShowsError() {
        KWICApplication.textPath = "input.txt";
        KWICApplication.stopWordPath = "stopwords.txt";

        KWICController controller = new KWICController();
        controller.contextWindowField = new TextField("-5");
        controller.outputTextArea = new TextArea();

        controller.onRunButtonClick();

        String output = controller.outputTextArea.getText();
        assertTrue(output.contains("Por favor, insira um número válido"));
    }

    @Test
    void testOnRunButtonClick_whenFrameworkFails_setsErrorMessage() {
        KWICController controller = new KWICController();

        controller.outputTextArea = new TextArea();
        controller.contextWindowField = new TextField("1");

        KWICApplication.textPath = "caminho/invalido.txt";
        KWICApplication.stopWordPath = "stopwords/naoexiste.txt";

        controller.onRunButtonClick();

        String output = controller.outputTextArea.getText();
        assertTrue(output.startsWith("Erro ao executar o algoritmo:"));
    }

    @Test
    void testOnRunButtonClick_executesSuccessfully() throws IOException {
        File inputFile = File.createTempFile("input", ".txt");
        File stopFile = File.createTempFile("stopwords", ".txt");

        Files.writeString(inputFile.toPath(), "cachorro gato cavalo cachorro");
        Files.writeString(stopFile.toPath(), "gato");

        KWICApplication.textPath = inputFile.getAbsolutePath();
        KWICApplication.stopWordPath = stopFile.getAbsolutePath();

        KWICController controller = new KWICController();
        controller.outputTextArea = new TextArea();
        controller.contextWindowField = new TextField("1");

        controller.onRunButtonClick();

        String output = controller.outputTextArea.getText();
        assertFalse(output.isEmpty());

        inputFile.delete();
        stopFile.delete();
    }

}