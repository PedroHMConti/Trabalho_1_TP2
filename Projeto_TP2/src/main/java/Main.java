import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        main(args, new EscreveInputFile());
    }

    public static void main(String[] args, EscreveInputFile escreveInputFile) {
        Scanner sc = new Scanner(System.in);
        int windowSize;
        String linha;

        System.out.print("Digite o tamanho da janela de contexto (numero >= 0): ");

        while (true) {
            try {
                windowSize = sc.nextInt();
                sc.nextLine();
                if (windowSize >= 0) {
                    break;
                } else {
                    System.out.print("A janela deve ser um numero >= 0, digite novamente: ");
                }
            } catch (InputMismatchException e) {
                System.out.print("A janela deve ser um numero >= 0, digite novamente: ");
                sc.nextLine();
            }
        }

        System.out.print("Digite os contextos, para finalizar digite fim: ");
        while (!(linha = sc.nextLine()).equalsIgnoreCase("fim")) {
            if (!linha.trim().isEmpty()) {
                escreveInputFile.escreveNoArquivoInput(linha);
            }
        }

        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorage = new DataStorage(wfapp, stopWordFilter, windowSize);
        KeyWordContextGenerator keyWordContextGenerator = new KeyWordContextGenerator(wfapp, dataStorage);
        wfapp.run(FilePaths.INPUT_FILE);
    }
}
