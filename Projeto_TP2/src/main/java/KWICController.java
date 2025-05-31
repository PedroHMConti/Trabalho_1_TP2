import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.util.function.Function;

public class KWICController {

    public interface FileChooserProvider {
        File showOpenDialog();
    }

    public static String getPath(FileChooserProvider fileChooserProvider) {
        File selectedFile = fileChooserProvider.showOpenDialog();
        return (selectedFile != null) ? selectedFile.getAbsolutePath() : "";
    }

    public static String getPath() {
        return getPath(() -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selecionar Arquivo:");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Arquivos de texto (*.txt)", "*.txt"));
            return fileChooser.showOpenDialog(null);
        });
    }

    public void setFields(File selectedFile) {
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            try {
                String conteudo = new String(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
                inputTextArea.setText(conteudo);
            } catch (Exception e) {
                inputTextArea.setText("Erro ao ler o arquivo: " + e.getMessage());
            }
        } else {
            filePathField.clear();
            inputTextArea.clear();
        }
    }

    @FXML
    TextArea inputTextArea;

    @FXML
    TextArea stopWordsTextArea;

    @FXML
    Label textFile;

    @FXML
    TextField filePathField;

    @FXML
    Button loadStopWordsButton;

    @FXML
    TextField stopWordsPathField;

    @FXML
    TextArea outputTextArea;

    @FXML
    TextField contextWindowField;

    @FXML
    protected void onLoadFileButtonClick() {
        String path = getPath();
        handleFileLoad(path);
    }

    void handleFileLoad(String path) {
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            setFields(file);
            KWICApplication.textPath = path;
        }
    }

    @FXML
    protected void onLoadStopWordsClick() {
        String path = getPath();
        handleStopWordsLoad(path);
    }

    void handleStopWordsLoad(String path) {
        if (path != null && !path.isEmpty()) {
            File file = new File(path);
            stopWordsPathField.setText(path);
            KWICApplication.stopWordPath = path;

            try {
                String conteudo = new String(Files.readAllBytes(file.toPath()));
                stopWordsTextArea.setText(conteudo);
            } catch (Exception e) {
                stopWordsTextArea.setText("Erro ao ler stop words: " + e.getMessage());
            }
        } else {
            stopWordsPathField.clear();
            stopWordsTextArea.clear();
        }
    }

    public void loadInitialFiles(String inputPath, String stopWordsPath) {
        try {
            File inputFile = new File(inputPath);
            if (inputFile.exists()) {
                setFields(inputFile);
                KWICApplication.textPath = inputFile.getAbsolutePath();
            }

            File stopFile = new File(stopWordsPath);
            if (stopFile.exists()) {
                stopWordsPathField.setText(stopFile.getAbsolutePath());
                KWICApplication.stopWordPath = stopFile.getAbsolutePath();

                String conteudo = new String(Files.readAllBytes(stopFile.toPath()));
                stopWordsTextArea.setText(conteudo);
            }

        } catch (Exception e) {
            inputTextArea.setText("Erro ao carregar arquivos padrão: " + e.getMessage());
        }
    }

    @FXML
    public void initialize() {
        loadInitialFiles(FilePaths.INPUT_FILE, FilePaths.STOP_WORDS_FILE);
    }

    @FXML
    protected void onRunButtonClick() {
        String inputPath = KWICApplication.textPath;
        String stopPath = KWICApplication.stopWordPath;

        if (inputPath == null || inputPath.isEmpty()) {
            outputTextArea.setText("Erro: Nenhum arquivo de input carregado.");
            return;
        }
        if (stopPath == null || stopPath.isEmpty()) {
            outputTextArea.setText("Erro: Nenhum arquivo de stop words carregado.");
            return;
        }

        int contextSize = 0;

        try {
            contextSize = Integer.parseInt(contextWindowField.getText());
            if (contextSize < 0) {
                throw new NumberFormatException("Valor negativo");
            }
        } catch (NumberFormatException e) {
            outputTextArea.setText("Por favor, insira um número válido (>= 0) para o tamanho da janela de contexto.");
            return;
        }

        try {
            WordFrequencyFramework wfapp = new WordFrequencyFramework();
            StopWordFilter stopWordFilter = new StopWordFilter(wfapp, stopPath);
            DataStorage dataStorage = new DataStorage(wfapp, stopWordFilter, contextSize);
            KeyWordContextGenerator kwic = new KeyWordContextGenerator(
                    wfapp,
                    dataStorage,
                    lines -> outputTextArea.setText(String.join("\n", lines)));

            wfapp.run(inputPath);

        } catch (Exception e) {
            outputTextArea.setText("Erro ao executar o algoritmo:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
}
