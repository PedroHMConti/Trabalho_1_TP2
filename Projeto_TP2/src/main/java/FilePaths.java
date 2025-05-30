import java.nio.file.Paths;

public class FilePaths {
    public static final String INPUT_FILE = Paths.get("src", "main", "java", "Files", "Input.txt").toString();
    public static final String STOP_WORDS_FILE = Paths.get("src", "main", "java", "Files", "stopWords.txt").toString();
    public static final String STOP_WORDS_FILE_TEST = Paths.get("src", "test", "java", "stopWordsTeste.txt").toString();
}
