package lu.uni.serval.json.aggregator.writer;

import com.fasterxml.jackson.databind.JsonNode;
import lu.uni.serval.json.aggregator.Helpers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class JsonWriterTest {
    private static File outputFolder = null;

    @BeforeAll
    static void createOutputFolder() throws IOException {
        outputFolder = Helpers.createOutputFolder(JsonWriterTest.class);
    }

    @AfterAll
    static void cleanOutputFolder(){
        if(outputFolder != null){
            outputFolder.delete();
        }
    }


    @Test
    void testWriteToFile() throws IOException {
        final JsonNode json = Helpers.toJson("{\"title\": \"Java\",\"year\": \"2021\",\"genre\": \"horror\"}");
        final File output = new File(outputFolder, "testWriteToFile.json");

        JsonWriter.jsonWriter(json, output);

        final JsonNode root = Helpers.toJson(output);
        assertEquals(3, Helpers.childrenCount(root));
    }
}