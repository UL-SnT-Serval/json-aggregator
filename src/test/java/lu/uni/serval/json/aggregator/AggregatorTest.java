package lu.uni.serval.json.aggregator;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AggregatorTest {
    private static File outputFolder = null;

    @BeforeAll
    static void createOutputFolder() throws IOException {
        outputFolder = Helpers.createOutputFolder(AggregatorTest.class);
    }

    @AfterAll
    static void cleanOutputFolder(){
        if(outputFolder != null){
            outputFolder.delete();
        }
    }

    @Test
    void testWorkingConfigWithFilter() throws IOException, URISyntaxException {
        final File input = Helpers.getResourceFile("json/folder1");
        final File output = new File(outputFolder, "testWorkingConfigWithFilter.json");
        final String filter = "field1,field2";

        String[] args = {
                "-i",
                input.getAbsolutePath(),
                "-o",
                output.getAbsolutePath(),
                "-f",
                filter
        };

        Aggregator.main(args);

        final JsonNode root = Helpers.toJson(output);
        assertEquals(2, Helpers.childrenCount(root));
    }

    @Test
    void testWorkingConfigWithoutFilter() throws IOException, URISyntaxException {
        final File input = Helpers.getResourceFile("json/folder1");
        final File output = new File(outputFolder, "testWorkingConfigWithFilter.json");

        String[] args = {
                "-i",
                input.getAbsolutePath(),
                "-o",
                output.getAbsolutePath()
        };

        Aggregator.main(args);

        final JsonNode root = Helpers.toJson(output);
        assertEquals(2, Helpers.childrenCount(root));
    }
}