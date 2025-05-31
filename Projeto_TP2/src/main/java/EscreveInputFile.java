import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EscreveInputFile {
    File file;

    public EscreveInputFile() {
        this.file = new File(FilePaths.INPUT_FILE);
    }

    public EscreveInputFile(File customFile) {
        this.file = customFile;
    }

    public void escreveNoArquivoInput(String frase) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
            bw.write(frase);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}