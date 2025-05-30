package core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class DataStorage {
    List<String> linhas = new ArrayList<>();
    StopWordFilter stopWordFilter;
    List<Consumer<String>> wordEventsHandlers = new ArrayList<>();
    int windowSize;

    public DataStorage(WordFrequencyFramework wfapp, StopWordFilter stopWordFilter, int windowSize) {
        this.stopWordFilter = stopWordFilter;
        this.windowSize = windowSize;
        wfapp.registerForLoadEvent(this::load);
        wfapp.registerForDoworkEvent(this::produceKWICContext);
    }

    // Carrega o conteúdo do arquivo e limpa os caracteres não alfabéticos
    public void load(String pathToFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(new File(pathToFile)))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                linhas.add(linha.toLowerCase().replaceAll("[\\W_]+", " ").trim());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void produceKWICContext() {
        for (String linha : linhas) {
            List<String> palavras = new ArrayList<>(Arrays.asList(linha.split("\\s+")));

            for (int i = 0; i < palavras.size(); i++) {
                String palavraChave = palavras.get(i);
                if (!stopWordFilter.isStopWord(palavraChave)) {
                    // Criar uma cópia da lista para rotacionar sem afetar a original
                    List<String> rotacionada = new ArrayList<>(palavras);

                    // Rotacionar para colocar a palavra-chave na posição 0
                    Collections.rotate(rotacionada, -i);

                    List<String> rotacionadaComJanela;

                    try {
                        rotacionadaComJanela = rotacionada.subList(0, this.windowSize + 1);
                    } catch (IndexOutOfBoundsException e) {
//                      System.out.println("Janela maior do que o contexto, janela limitada para o tamanho do contexto");
                        rotacionadaComJanela = rotacionada.subList(0, rotacionada.size());
                    }

                    // Construir a string da frase rotacionada
                    String fraseRotacionada = String.join(" ", rotacionadaComJanela);

                    // Criar o contexto no formato solicitado
                    String contexto = "(from: " + linha + ")";

                    // Montar a linha final
                    String linhaRotacionadaEContexto = fraseRotacionada + " " + contexto;

                    // Enviar para os handlers
                    for (Consumer<String> handler : wordEventsHandlers) {
                        handler.accept(linhaRotacionadaEContexto);
                    }
                }
            }
        }
    }

    // Permite que outras classes registrem funções para receber as palavras
    public void registerForWordEvent(Consumer<String> handler) {
        wordEventsHandlers.add(handler);
    }
}
