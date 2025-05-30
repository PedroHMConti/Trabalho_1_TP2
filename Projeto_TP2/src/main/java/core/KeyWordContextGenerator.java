package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class KeyWordContextGenerator {
    private final List<String> kwicLines = new ArrayList<>();
    private final Consumer<List<String>> outputConsumer;

    public KeyWordContextGenerator(WordFrequencyFramework wfapp, DataStorage dataStorage) {
        this(wfapp, dataStorage, null); // passa null para outputConsumer
    }

    public KeyWordContextGenerator(WordFrequencyFramework wfapp, DataStorage dataStorage, Consumer<List<String>> outputConsumer) {
        this.outputConsumer = outputConsumer;
        dataStorage.registerForWordEvent(this::storeLine);
        wfapp.registerForEndEvent(this::printKWIC);
    }

    private void storeLine(String line) {
        kwicLines.add(line);
    }

    private void printKWIC() {
        Collections.sort(kwicLines);

        if (outputConsumer != null) {
            outputConsumer.accept(kwicLines);
        } else {
            for (String line : kwicLines) {
                System.out.println(line);
            }
        }
    }
}
