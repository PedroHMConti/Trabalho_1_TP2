import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        //input do programa
        Scanner sc = new Scanner(System.in);
        int windowSize;
        EscreveInputFile escreveInputFile = new EscreveInputFile();
        String linha;
        System.out.print("Digite o tamanho da janela de contexto: ");
        windowSize = sc.nextInt();
        System.out.print("Digite os contextos, para finalizar digite fim: ");
        while(!(linha = sc.nextLine()).equalsIgnoreCase("fim")){
            escreveInputFile.escreveNoArquivoInput(linha);
        }

        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorage = new DataStorage(wfapp,stopWordFilter, windowSize);
        KeyWordContextGenerator keyWordContextGenerator  = new KeyWordContextGenerator(wfapp,dataStorage);
        wfapp.run("/home/marcos/Documents/arquivosTP2/inputFile.txt");
    }
}