public class Main{
    public static void main(String[] args) {
        WordFrequencyFramework wfapp = new WordFrequencyFramework();
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);
        DataStorage dataStorage = new DataStorage(wfapp,stopWordFilter);
        KeyWordContextGenerator keyWordContextGenerator  = new KeyWordContextGenerator(wfapp,dataStorage);
        wfapp.run("C:\\Users\\pedro\\OneDrive\\Área de Trabalho\\projetos_java\\Hollywood_wordCount\\src\\Files\\inputFile.txt");
    }
}