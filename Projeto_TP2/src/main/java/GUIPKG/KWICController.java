package GUIPKG;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import utils.FilePaths;

import core.WordFrequencyFramework;
import core.StopWordFilter;
import core.DataStorage;
import core.KeyWordContextGenerator;

import java.io.File;
import java.nio.file.Files;
import java.util.Scanner;

public class KWICController {

    public static String getPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Arquivo:");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Arquivos de texto (*.txt)",
                "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter);

        File selectedFile = fileChooser.showOpenDialog(null);
        return (selectedFile != null) ? selectedFile.getAbsolutePath() : "";
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
    private TextArea inputTextArea;

    @FXML
    private TextArea stopWordsTextArea;

    @FXML
    private Label textFile;

    @FXML
    private TextField filePathField;

    @FXML
    private Button loadStopWordsButton;

    @FXML
    private TextField stopWordsPathField;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextField contextWindowField;

    @FXML
    protected void onLoadFileButtonClick() {
        String path = getPath();
        if (!path.isEmpty()) {
            File file = new File(path);
            setFields(file);
            KWICApplication.textPath = path;
        }
    }

    @FXML
    protected void onLoadStopWordsClick() {
        String path = getPath();
        if (!path.isEmpty()) {
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
