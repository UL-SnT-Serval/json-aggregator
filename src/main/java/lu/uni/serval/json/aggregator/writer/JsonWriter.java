package lu.uni.serval.json.aggregator.writer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonWriter {
    private JsonWriter() {}

    public static void jsonWriter(final JsonNode json, final File output) throws IOException {
        try(JsonGenerator generator = new JsonFactory().createGenerator(output, JsonEncoding.UTF8)){
            generator.setCodec(new ObjectMapper());
            generator.setPrettyPrinter(new DefaultPrettyPrinter());

            generator.writeTree(json);
        }
    }
}
