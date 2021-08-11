package lu.uni.serval.json.aggregator;

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
import lu.uni.serval.json.aggregator.exception.InputException;
import lu.uni.serval.json.aggregator.merger.JsonMerger;
import lu.uni.serval.json.aggregator.reader.JsonReader;
import lu.uni.serval.json.aggregator.writer.JsonWriter;
import org.apache.commons.cli.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aggregator {
    private static final Logger logger = LogManager.getLogger(Aggregator.class);

    public static void main(String[] args) {
        try {
            final CommandLine cmd = readArguments(args);

            final File input = new File(cmd.getOptionValue("input"));
            if(!input.exists()){
                throw new IOException(String.format("Input folder '%s' does not exist!", input.getAbsolutePath()));
            }

            final File output = new File(cmd.getOptionValue("output"));
            if(!output.getParentFile().exists()){
                throw new IOException(String.format("Output folder '%s' does not exist!", output.getParentFile().getAbsolutePath()));
            }

            final Set<String> filter = parseFilter(cmd.getOptionValue("filter"));

            final Set<JsonNode> jsonTrees = JsonReader.readJsonFiles(input);
            final JsonNode jsonMerged = JsonMerger.merge(jsonTrees, filter);

            JsonWriter.jsonWriter(jsonMerged, output);

        } catch (ParseException e) {
            logger.printf(Level.ERROR,
                    "Failed to parse command line arguments: [%s] %s",
                    e.getClass().getSimpleName(),
                    e.getMessage()
            );

            System.exit(-1);
        } catch (IOException | InputException e) {
            logger.printf(Level.ERROR,
                    "An error occurred during the processing stopping the process: [%s] %s",
                    e.getClass().getSimpleName(),
                    e.getMessage()
            );

            System.exit(-1);
        } catch (InterruptedException e) {
            logger.error("Process interrupted");
            Thread.currentThread().interrupt();
            System.exit(-1);
        }
    }

    private static CommandLine readArguments(String[] args) throws ParseException {
        final Options options = new Options();
        final CommandLineParser parser = new DefaultParser();

        final Option folder = new Option("i", "input", true, "path to the folder containing the JSON files to merge");
        folder.setRequired(true);
        options.addOption(folder);

        final Option output = new Option("o", "output", true, "path to the output JSON file");
        output.setRequired(true);
        options.addOption(output);

        final Option filter = new Option("f", "filter", true, "comma separated list of all the field to keep in the output");
        filter.setRequired(false);
        options.addOption(filter);

        return parser.parse(options, args);
    }

    private static Set<String> parseFilter(String filterString){
        if(filterString == null){
            return Collections.emptySet();
        }

        return Stream.of(filterString.split(","))
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .collect(Collectors.toSet());
    }
}
