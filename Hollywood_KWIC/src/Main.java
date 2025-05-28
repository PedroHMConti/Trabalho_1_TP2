import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        //input do programa
        Scanner sc = new Scanner(System.in);
        EscreveInputFile escreveInputFile = new EscreveInputFile();
        String linha;
        while(!(linha = sc.nextLine()).equalsIgnoreCase("fim")){
            escreveInputFile.escreveNoArquivoInput(linha);
        }

        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorage = new DataStorage(wfapp,stopWordFilter);
        KeyWordContextGenerator keyWordContextGenerator  = new KeyWordContextGenerator(wfapp,dataStorage);
        wfapp.run("C:\\Users\\pedro\\OneDrive\\Área de Trabalho\\projetos_java\\Hollywood_KWIC\\src\\Files\\inputFile.txt");
    }
}
