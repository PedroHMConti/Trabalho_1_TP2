package core;
import org.junit.jupiter.api.Test;

import utils.FilePaths;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TesteIntegracao {

    // Teste de integração para verificar o fluxo completo do framework
    // Utiliza as classes WordFrequencyFramework, StopWordFilter, DataStorage e KeyWordContextGenerator

    @Test
    void testeIntegracaoInstrucao() throws Exception {
        // Configurar o WordFrequencyFramework
        WordFrequencyFramework wfapp = new WordFrequencyFramework();

        // Configurar o StopWordFilter para usar o arquivo stopWordsTeste.txt
        StopWordFilter stopWordFilter = new StopWordFilter(wfapp) {
            @Override
            public void load(String pathToFile) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(FilePaths.STOP_WORDS_FILE));
                    String linha;
                    while ((linha = br.readLine()) != null) {
                        stopWords.addAll(List.of(linha.split("\\s+")));
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };

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
}
