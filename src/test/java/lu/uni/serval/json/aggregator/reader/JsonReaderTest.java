package lu.uni.serval.json.aggregator.reader;

/*-
 * #%L
 * JSON Aggregator
 * %%
 * Copyright (C) 2021 University of Luxembourg, Renaud RWEMALIKA
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import com.fasterxml.jackson.databind.JsonNode;
import lu.uni.serval.json.aggregator.Helpers;
import lu.uni.serval.json.aggregator.exception.InputException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderTest {
    @Test
    void testLoadFromValidFolder() throws IOException, URISyntaxException, InterruptedException, InputException {
        final File input = Helpers.getResourceFile("json/folder1");
        final Set<JsonNode> jsonNodes = JsonReader.readJsonFiles(input);

        assertEquals(2, jsonNodes.size());
    }

    @Test
    void testLoadFromEmptyFolder() throws IOException, URISyntaxException {
        final File input = Helpers.getResourceFile("json/emptyFolder");

        final Throwable exception = assertThrows(InputException.class, () -> JsonReader.readJsonFiles(input));
        final String errorMessage = String.format("The input folder '%s' does not contain any JSON file (*.json).", input.getAbsolutePath());

        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testLoadFromNonExistingFolder() throws IOException, URISyntaxException {
        final File jsonFolder = Helpers.getResourceFile("json");
        final File nonExisting = new File(jsonFolder, "nonExisting");

        final Throwable exception = assertThrows(InputException.class, () -> JsonReader.readJsonFiles(nonExisting));
        final String errorMessage = String.format("Input folder '%s' does not exist!", nonExisting.getAbsolutePath());

        assertEquals(errorMessage, exception.getMessage());
    }
}
