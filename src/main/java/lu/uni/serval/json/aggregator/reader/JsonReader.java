package lu.uni.serval.json.aggregator.reader;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class responsible for reading the JSON tree from a JSON file on the file system.
 */
public class JsonReader {

    private static final Logger logger = LogManager.getLogger(JsonReader.class);

    private JsonReader() {}

    /**
     * Static method which reads the JSON file passed as input and convert it to a JSON tree.
     *
     * @param folder Absolute path to the folder containing the JSON files. Note that only files with the extension '.json' are considered.
     * @return Root of the JSON tree. If one file is malformed, it will be ignored.
     * @throws InterruptedException The method is multithreaded to parallelize the parsing of multiple JSON files. As such it can raise an InteruptedExcetion.
     */
    public static Set<JsonNode> readJsonFiles(final File folder) throws InterruptedException {
        final List<Task> tasks = Stream.of(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".json"))))
                .map(Task::new)
                .collect(Collectors.toList());

        final int numberThreads = Math.max(Runtime.getRuntime().availableProcessors(), tasks.size());
        final ExecutorService executor = Executors.newFixedThreadPool(numberThreads);

        final Set<JsonNode> jsonTrees = executor.invokeAll(tasks).stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        logger.printf(Level.ERROR,
                                "Failed to parse json file: [%s] %s",
                                e.getClass().getSimpleName(),
                                e.getMessage()
                        );

                        Thread.currentThread().interrupt();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        executor.shutdown();

        return jsonTrees;
    }

    private static class Task implements Callable<JsonNode> {
        private final File jsonFile;

        private Task(File jsonFile) {
            this.jsonFile = jsonFile;
        }

        @Override
        public JsonNode call() throws Exception {
            final JsonFactory factory = new JsonFactory();
            final ObjectMapper mapper = new ObjectMapper(factory);

            return mapper.readTree(this.jsonFile);
        }
    }
}
