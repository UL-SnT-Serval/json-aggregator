package lu.uni.serval.json.aggregator.merger;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lu.uni.serval.json.aggregator.Helpers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JsonMergerTest {
    private static JsonNode json1;
    private static JsonNode json2;

    @BeforeAll
    static void prepareJsonTrees() throws JsonProcessingException {
        json1 = Helpers.toJson("{\"title\": \"Java\",\"year\": \"2021\",\"genre\": \"horror\"}");
        json2 = Helpers.toJson("{\"title\": \"C++\",\"year\": \"2021\",\"genre\": \"horror\", \"editor\": \"Cilling House\"}");
    }

    @Test
    void testMergeTwoJsonWithNoFilter(){
        final Set<JsonNode> jsonTrees = new HashSet<>(2);
        jsonTrees.add(json1);
        jsonTrees.add(json2);

        final JsonNode merged = JsonMerger.merge(jsonTrees, Collections.emptySet());
        assertTrue(merged.isArray());

        final ArrayNode arrayNode = (ArrayNode) merged;
        assertEquals(2, Helpers.childrenCount(arrayNode));

        final JsonNode javaNode = Helpers.findChildByField(arrayNode, "title", "Java");
        assertNotNull(javaNode);
        assertEquals(3, Helpers.childrenCount(javaNode));

        final JsonNode cppNode = Helpers.findChildByField(arrayNode, "title", "C++");
        assertNotNull(cppNode);
        assertEquals(4, Helpers.childrenCount(cppNode));
    }

    @Test
    void testMergeTwoJsonWithFilter(){
        final Set<JsonNode> jsonTrees = new HashSet<>(2);
        jsonTrees.add(json1);
        jsonTrees.add(json2);

        final JsonNode merged = JsonMerger.merge(jsonTrees, Collections.singleton("title"));
        assertTrue(merged.isArray());

        final ArrayNode arrayNode = (ArrayNode) merged;
        assertEquals(2, Helpers.childrenCount(arrayNode));

        final JsonNode javaNode = Helpers.findChildByField(arrayNode, "title", "Java");
        assertNotNull(javaNode);
        assertEquals(1, Helpers.childrenCount(javaNode));

        final JsonNode cppNode = Helpers.findChildByField(arrayNode, "title", "C++");
        assertNotNull(cppNode);
        assertEquals(1, Helpers.childrenCount(cppNode));
    }
}