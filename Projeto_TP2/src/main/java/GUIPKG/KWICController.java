package GUIPKG;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class KWICController {
    public static String getPath() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecionar Arquivo:");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Arquivos de texto (*.txt", "*.txt");
        fileChooser.getExtensionFilters().add(extensionFilter); // Limita a arquivos de texto

        File selectedFile = fileChooser.showOpenDialog(null); // Abre a janela de escolher arquivo
        String absolutePath = "";
        if (selectedFile != null) {
            absolutePath = selectedFile.getAbsolutePath();
        }
        return absolutePath;
    }

    public void setFields(File selectedFile) {
        if (selectedFile != null) {
            textFile.setText("Arquivo selecionado: " + selectedFile.getName());
            filePathField.setText(selectedFile.getAbsolutePath());
            try {
                String conteudo = new String(java.nio.file.Files.readAllBytes(selectedFile.toPath()));
                inputTextArea.setText(conteudo); // Coloca o texto na caixa
            } catch (Exception e) {
                textFile.setText("Erro ao ler o arquivo: " + e.getMessage());
                inputTextArea.clear();
            }
        } else {
            textFile.setText("Nenhum arquivo selecionado");
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
    protected void onLoadFileButtonClick() {
        String path = getPath();
        File file = new File(path);
        setFields(file);
        KWICApplication.textPath = path;
    }

    @FXML
    protected void onLoadStopWordsClick() {
        System.out.println("Carregando palavras de parada...");
    }

    

}
