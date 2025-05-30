
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TesteIntegracao {

    // Teste de integração para verificar o fluxo completo do framework
    // Utiliza as classes WordFrequencyFramework, StopWordFilter, DataStorage e KeyWordContextGenerator

    @Test
    void testeIntegracaoInstrucao() throws Exception {
        // Configurar o WordFrequencyFramework
        WordFrequencyFramework wfapp = new WordFrequencyFramework();

        // Configurar o StopWordFilter para usar o arquivo stopWordsTeste.txt
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp, "src/test/java/stopWordsTeste.txt");

        // Configurar o DataStorage
        DataStorage dataStorage = new DataStorage(wfapp, stopWordFilter,3);

        // Configurar o KeyWordContextGenerator
        KeyWordContextGenerator keyWordContextGenerator = new KeyWordContextGenerator(wfapp, dataStorage);

        // Criar um arquivo temporário para simular o arquivo de entrada
        Path tempInputFile = Files.createTempFile("Input", ".txt");
        Files.writeString(tempInputFile, "The quick brown fox\n");
        Files.writeString(tempInputFile, "A brown cat sat\n", java.nio.file.StandardOpenOption.APPEND);
        Files.writeString(tempInputFile, "The cat is brown\n", java.nio.file.StandardOpenOption.APPEND);
        // Redirecionar a saída padrão para capturar o console
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Executar o fluxo do framework com o caminho do arquivo temporário
        wfapp.run(tempInputFile.toString());

        // Restaurar a saída padrão
        System.setOut(System.out);

        // Verificar o KWIC gerado
        List<String> expectedKWIC = List.of(
                "brown cat sat a (from: a brown cat sat)",
                "brown fox the quick (from: the quick brown fox)",
                "brown the cat is (from: the cat is brown)",
                "cat is brown the (from: the cat is brown)",
                "cat sat a brown (from: a brown cat sat)",
                "fox the quick brown (from: the quick brown fox)",
                "quick brown fox the (from: the quick brown fox)"
        );

        String[] actualKWIC = outputStream.toString().trim().split("\r?\n");
        assertEquals(expectedKWIC, List.of(actualKWIC));

        // Excluir o arquivo temporário
        Files.delete(tempInputFile);
    }

    // Teste para caso a entrada seja vazia, ou seja o arquivo de entrada está vazio

    @Test
    void testeIntegracaoVazio() throws Exception {
        // Configurar o WordFrequencyFramework
        WordFrequencyFramework wfapp = new WordFrequencyFramework();

        // Configurar o StopWordFilter para usar o arquivo stopWordsTeste.txt
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp);

        // Configurar o DataStorage
        DataStorage dataStorage = new DataStorage(wfapp, stopWordFilter,3);

        // Configurar o KeyWordContextGenerator
        KeyWordContextGenerator keyWordContextGenerator = new KeyWordContextGenerator(wfapp, dataStorage);

        // Criar um arquivo temporário para simular o arquivo de entrada
        Path tempInputFile = Files.createTempFile("Input", ".txt");
        // Não escrever nada no arquivo temporário para simular entrada vazia
        // Redirecionar a saída padrão para capturar o console
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Executar o fluxo do framework com o caminho do arquivo temporário
        wfapp.run(tempInputFile.toString());

        // Restaurar a saída padrão
        System.setOut(System.out);

        // Verificar se a saída está vazia

        String[] actualKWIC = outputStream.toString().trim().split("\r?\n");
        // Compara com uma lista vazia, pois não deve haver saída para um arquivo de entrada vazio


        assertEquals(List.of(""), List.of(actualKWIC));

        // Excluir o arquivo temporário
        Files.delete(tempInputFile);
    }

    @Test
    void testeIntegracaoInstrucaoVarios() throws Exception {
        // Configurar o WordFrequencyFramework
        WordFrequencyFramework wfapp = new WordFrequencyFramework();

        // Configurar o StopWordFilter para usar o arquivo stopWordsTeste.txt
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp, "src/test/java/stopWordsTeste.txt");

        // Configurar o DataStorage de acordo com o tamanho da janela de contexto
        DataStorage dataStorage = new DataStorage(wfapp, stopWordFilter,2);

        // Configurar o KeyWordContextGenerator
        KeyWordContextGenerator keyWordContextGenerator = new KeyWordContextGenerator(wfapp, dataStorage);

        // Criar um arquivo temporário para simular o arquivo de entrada
        Path tempInputFile = Files.createTempFile("Input", ".txt");
        Files.writeString(tempInputFile, "The quick brown fox\n");
        Files.writeString(tempInputFile, "A brown cat sat\n", java.nio.file.StandardOpenOption.APPEND);
        Files.writeString(tempInputFile, "The cat is brown\n", java.nio.file.StandardOpenOption.APPEND);
        // Redirecionar a saída padrão para capturar o console
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Executar o fluxo do framework com o caminho do arquivo temporário
        wfapp.run(tempInputFile.toString());

        // Restaurar a saída padrão
        System.setOut(System.out);

        // Verificar o KWIC gerado
        List<String> expectedKWIC = List.of(
                "brown cat sat (from: a brown cat sat)",
                "brown fox the (from: the quick brown fox)",
                "brown the cat (from: the cat is brown)",
                "cat is brown (from: the cat is brown)",
                "cat sat a (from: a brown cat sat)",
                "fox the quick (from: the quick brown fox)",
                "quick brown fox (from: the quick brown fox)"
        );

        String[] actualKWIC = outputStream.toString().trim().split("\r?\n");
        assertEquals(expectedKWIC, List.of(actualKWIC));

        // Salva o conteudo do arquivo de stopwords original para restaurar depois
        Path stopWordsPath = Path.of("src/test/java/stopWordsTeste.txt");
        String originalStopWords = Files.readString(stopWordsPath);

        // Apaga as stopwords do arquivo original
        Files.writeString(stopWordsPath, "");


        // Crie um novo framework para o segundo teste
        WordFrequencyFramework wfapp2 = new WordFrequencyFramework();
        StopWordFilter stopWordFilterNoStop = new StopWordFilter(wfapp2, stopWordsPath.toString());
        DataStorage dataStorageNoStop = new DataStorage(wfapp2, stopWordFilterNoStop, 2);
        KeyWordContextGenerator keyWordContextGeneratorNoStop = new KeyWordContextGenerator(wfapp2, dataStorageNoStop);

        ByteArrayOutputStream outputStreamNoStop = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStreamNoStop));

        // Executa o novo framework
        wfapp2.run(tempInputFile.toString());


        System.setOut(System.out);

        List<String> expectedKWICNoStop = List.of(
            "a brown cat (from: a brown cat sat)",
            "brown cat sat (from: a brown cat sat)",
            "brown fox the (from: the quick brown fox)",
            "brown the cat (from: the cat is brown)",
            "cat is brown (from: the cat is brown)",
            "cat sat a (from: a brown cat sat)",
            "fox the quick (from: the quick brown fox)",
            "is brown the (from: the cat is brown)",
            "quick brown fox (from: the quick brown fox)",
            "sat a brown (from: a brown cat sat)",
            "the cat is (from: the cat is brown)",
            "the quick brown (from: the quick brown fox)"
        );


        String[] actualKWICNoStop = outputStreamNoStop.toString().trim().split("\r?\n");
        // Restaurar o arquivo de stopwords original
        Files.writeString(stopWordsPath, originalStopWords);

        assertEquals(expectedKWICNoStop, List.of(actualKWICNoStop));


        // Excluir o arquivo temporário
        Files.delete(tempInputFile);

    }
}
