package lu.uni.serval.json.aggregator.writer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Class responsible for writing the JSON tree to a JSON file on the file system.
 */
public class JsonWriter {
    private JsonWriter() {}

    /**
     * Static method which writes the JSON tree passed as input to a file for which the absolute path is passed as input.
     *
     * @param json Root of the JSON tree to write to file
     * @param output Absolute path to the output file where to write the JSON file. If the file is not empty it will be overwritten.
     * @throws IOException An IO exception is raised if the file cannot be written.
     */
    public static void jsonWriter(final JsonNode json, final File output) throws IOException {
        try(JsonGenerator generator = new JsonFactory().createGenerator(output, JsonEncoding.UTF8)){
            generator.setCodec(new ObjectMapper());
            generator.setPrettyPrinter(new DefaultPrettyPrinter());

            generator.writeTree(json);
        }
    }
}
