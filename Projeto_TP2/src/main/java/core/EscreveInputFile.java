package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import utils.FilePaths;

public class EscreveInputFile {
    File file = new File(FilePaths.INPUT_FILE);
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
