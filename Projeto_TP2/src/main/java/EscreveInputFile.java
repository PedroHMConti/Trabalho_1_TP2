import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscreveInputFile {
    File file = new File("C:\\Users\\pedro\\OneDrive\\Área de Trabalho\\projetos_java\\Projeto_TP2\\src\\main\\java\\Files\\Input.txt");
    public void escreveNoArquivoInput(String frase){
        try (FileWriter fw = new FileWriter(file,true)) {
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(frase);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
