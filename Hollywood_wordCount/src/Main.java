public class Main{
    public static void main(String[] args) {
        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorage = new DataStorage(wfapp,stopWordFilter);
        WordFrequencyCounter wordFrequencyCounter = new WordFrequencyCounter(wfapp,dataStorage);
        wfapp.run("C:\\Users\\pedro\\OneDrive\\Área de Trabalho\\projetos_java\\Hollywood_wordCount\\src\\Files\\inputFile.txt");
    }
}