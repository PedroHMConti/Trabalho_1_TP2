import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscreveInputFile {
    File file = new File("/home/marcos/Documents/arquivosTP2/inputFile.txt");
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
