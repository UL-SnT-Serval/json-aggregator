package lu.uni.serval.json.aggregator;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

public class Helpers {
    public static File getResourceFile(String name) throws IOException, URISyntaxException {
        final URL resource = Helpers.class.getClassLoader().getResource(name);

        if (resource == null) {
            throw new IOException(String.format("Failed to locate resource: %s", name));
        }

        return Paths.get(resource.toURI()).toFile();
    }

    public static int childrenCount(JsonNode node){
        final Iterator<JsonNode> elements = node.elements();

        int count = 0;
        while (elements.hasNext()){
            elements.next();
            ++count;
        }

        return count;
    }

    public static JsonNode findChildByField(ArrayNode node, String field, String value){
        final Iterator<JsonNode> elements = node.elements();

        while (elements.hasNext()){
            JsonNode element = elements.next();
            final JsonNode target = element.get(field);
            if(target != null && target.isTextual() && target.textValue().equals(value)){
                return element;
            }
        }

        return null;
    }

    public static JsonNode toJson(String json) throws JsonProcessingException {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        return mapper.readTree(json);
    }

    public static JsonNode toJson(File json) throws IOException {
        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        return mapper.readTree(json);
    }

    public static File createOutputFolder(Class<?> origin) throws IOException {
        final File tmpFolder = new File(System.getProperty("java.io.tmpdir"));
        final String name = String.format("%s-%d", origin.getSimpleName(), System.currentTimeMillis());

        final File outputFolder = new File(tmpFolder, name);

        if(!outputFolder.mkdir()){
            throw new IOException(String.format(
                    "Failed to create output directory '%s' for test suite %s",
                    outputFolder.getAbsolutePath(),
                    origin.getCanonicalName()
            ));
        }

        return outputFolder;
    }

    public static String runMain(List<String> args) throws IOException, InterruptedException {
        final List<String> command = new LinkedList<>();

        command.add("java");
        command.add("-cp");
        command.add(System.getProperty("java.class.path"));
        command.add(Aggregator.class.getName());
        command.addAll(args);

        final ProcessBuilder pb = new ProcessBuilder(command);
        final Process process = pb.start();

        final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        final StringJoiner joiner = new StringJoiner(System.getProperty("line.separator"));

        reader.lines().iterator().forEachRemaining(joiner::add);
        process.waitFor();
        process.destroy();

        return joiner.toString();
    }
}
