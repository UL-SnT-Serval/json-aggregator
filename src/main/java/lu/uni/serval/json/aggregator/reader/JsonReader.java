package lu.uni.serval.json.aggregator.reader;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonReader {
    private static final Logger logger = LogManager.getLogger(JsonReader.class);

    public static Set<JsonNode> readJsonFiles(final File folder) throws IOException, InterruptedException {
        final List<Task> tasks = Stream.of(Objects.requireNonNull(folder.listFiles((dir, name) -> name.endsWith(".json"))))
                .map(Task::new)
                .collect(Collectors.toList());

        final int numberThreads = Math.max(Runtime.getRuntime().availableProcessors(), tasks.size());
        final ExecutorService executor = Executors.newFixedThreadPool(numberThreads);

        return executor.invokeAll(tasks).stream()
                .map(f -> {
                    try {
                        return f.get();
                    } catch (Exception e) {
                        logger.printf(Level.ERROR,
                                "Failed to parse json file: [%s] %s",
                                e.getClass().getSimpleName(),
                                e.getMessage()
                        );
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
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
