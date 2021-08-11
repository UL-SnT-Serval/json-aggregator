package lu.uni.serval.json.aggregator;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void testWorkingConfigWithoutFilter() throws IOException, URISyntaxException, InterruptedException {
        final File input = Helpers.getResourceFile("json/folder1");
        final File output = new File(outputFolder, "testWorkingConfigWithFilter.json");

        final List<String> args = new ArrayList<>(4);
        args.add("-i");
        args.add(input.getAbsolutePath());
        args.add("-o");
        args.add(output.getAbsolutePath());

        Helpers.runMain(args);

        final JsonNode root = Helpers.toJson(output);
        assertEquals(2, Helpers.childrenCount(root));
    }

    @Test
    void testEmptyConfigWithoutFilter() throws IOException, InterruptedException {
        final String console = Helpers.runMain(Collections.emptyList());
        assertTrue(console.contains("Missing required options: i, o"));
    }

    @Test
    void testEmptyConfigMissingOneArgument() throws IOException, InterruptedException {
        final File output = new File(outputFolder, "testWorkingConfigWithFilter.json");

        final List<String> args = new ArrayList<>(4);
        args.add("-o");
        args.add(output.getAbsolutePath());

        final String console = Helpers.runMain(args);
        assertTrue(console.contains("Missing required option: i"));
    }

    @Test
    void testWithInvalidInputFolder() throws IOException, InterruptedException, URISyntaxException {
        final File input = new File(Helpers.getResourceFile("json"), "nonExisting");
        final File output = new File(outputFolder, "testWorkingConfigWithFilter.json");

        final List<String> args = new ArrayList<>(4);
        args.add("-i");
        args.add(input.getAbsolutePath());
        args.add("-o");
        args.add(output.getAbsolutePath());

        final String console = Helpers.runMain(args);
        final String errorMessage = String.format("Input folder '%s' does not exist!", input.getAbsolutePath());
        assertTrue(console.contains(String.format(errorMessage)));
    }
}