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
    private static JsonNode json3;

    @BeforeAll
    static void prepareJsonTrees() throws JsonProcessingException {
        json1 = Helpers.toJson("{\"title\": \"Java\",\"year\": \"2021\",\"genre\": \"horror\"}");
        json2 = Helpers.toJson("{\"title\": \"C++\",\"year\": \"2021\",\"genre\": \"horror\", \"editor\": \"Cilling House\"}");
        json3 = Helpers.toJson("[" +
                "{\"title\": \"Matlab\",\"year\": \"2021\",\"genre\": \"horror\", \"editor\": \"Cilling House\"},"+
                "{\"title\": \"Python\",\"year\": \"2021\",\"genre\": \"horror\", \"editor\": \"Cilling House\"},"+
                "{\"title\": \"JavaScript\",\"year\": \"2021\",\"genre\": \"horror\", \"editor\": \"Cilling House\"}"+
        "]");
    }

    @Test
    void testMergeTwoJsonWithNoFilter(){
        final Set<JsonNode> jsonTrees = new HashSet<>(2);
        jsonTrees.add(json1.deepCopy());
        jsonTrees.add(json2.deepCopy());

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
        jsonTrees.add(json1.deepCopy());
        jsonTrees.add(json2.deepCopy());

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

    @Test
    void testMergeWithJsonArray(){
        final Set<JsonNode> jsonTrees = new HashSet<>(2);
        jsonTrees.add(json1.deepCopy());
        jsonTrees.add(json3.deepCopy());

        final JsonNode merged = JsonMerger.merge(jsonTrees, Collections.singleton("title"));
        assertTrue(merged.isArray());

        final ArrayNode arrayNode = (ArrayNode) merged;
        assertEquals(4, Helpers.childrenCount(arrayNode));

        final JsonNode javaNode = Helpers.findChildByField(arrayNode, "title", "Java");
        assertNotNull(javaNode);
        assertEquals(1, Helpers.childrenCount(javaNode));

        final JsonNode pythonNode = Helpers.findChildByField(arrayNode, "title", "Python");
        assertNotNull(pythonNode);
        assertEquals(1, Helpers.childrenCount(pythonNode));

        final JsonNode matlabNode = Helpers.findChildByField(arrayNode, "title", "Matlab");
        assertNotNull(matlabNode);
        assertEquals(1, Helpers.childrenCount(matlabNode));

        final JsonNode jsNode = Helpers.findChildByField(arrayNode, "title", "JavaScript");
        assertNotNull(jsNode);
        assertEquals(1, Helpers.childrenCount(jsNode));
    }
}