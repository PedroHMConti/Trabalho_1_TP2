
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class WordFrequencyFramework {
    List<Consumer<String>> LoadEventHandlers = new ArrayList<>();
    List<Runnable> DoworkEventHandlers = new ArrayList<>();
    List<Runnable> EndEventHandlers = new ArrayList<>();

    public void registerForLoadEvent(Consumer<String> handler) {
        LoadEventHandlers.add(handler);
    }

    public void registerForDoworkEvent(Runnable handler) {
        DoworkEventHandlers.add(handler);
    }

    public void registerForEndEvent(Runnable handler) {
        EndEventHandlers.add(handler);
    }

    public void run(String s) {
        for (Consumer<String> h : this.LoadEventHandlers) {
            h.accept(s);
        }
        for (Runnable r : this.DoworkEventHandlers) {
            r.run();
        }
        for (Runnable r : this.EndEventHandlers) {
            r.run();
        }
    }
}
