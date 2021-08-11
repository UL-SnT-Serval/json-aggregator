package lu.uni.serval.json.aggregator.merger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

/**
 * Class responsible for merging different JSON trees based on some rules. Typically, we want to only keep a subset of
 * the fields present in the input files.
 */
public class JsonMerger {
    private JsonMerger() {}

    /**
     * Merges of set of JSON tree and outputs a single tree where the root node is an array. If the input trees are arrays,
     * then the algorithm visit each of the sub-nodes until a non-array type is discovered.
     *
     * @param jsonTrees Root nodes of JSON tree to be merged.
     * @param filter Fields names to keep in the output. If the list is null or empty, then all fields are kept.
     * @return Root node of a single tree which is a JSON array composed of the other trees with the filter applied.
     */
    public static JsonNode merge(Set<JsonNode> jsonTrees, Set<String> filter){
        final List<JsonNode> nodes = new ArrayList<>();

        for(JsonNode node: jsonTrees){
            nodes.addAll(filterNodes(node, filter));
        }

        final ArrayNode arrayNode = JsonNodeFactory.instance.arrayNode(nodes.size());
        arrayNode.addAll(nodes);

        return arrayNode;
    }

    private static List<JsonNode> filterNodes(JsonNode jsonNode, Set<String> filter){
        final List<JsonNode> nodes = new ArrayList<>();

        if(jsonNode.isArray()){
            final ArrayNode arrayNode = (ArrayNode) jsonNode;

            for (JsonNode childNode: arrayNode){
                nodes.addAll(filterNodes(childNode, filter));
            }
        }
        else if(jsonNode.isObject()){
            ObjectNode objectNode = (ObjectNode) jsonNode;

            if(filter != null && !filter.isEmpty()){
                Set<String> toRemove = new HashSet<>();
                for (Iterator<String> it = objectNode.fieldNames(); it.hasNext(); ) {
                    final String fieldName = it.next();
                    if(!filter.contains(fieldName)){
                        toRemove.add(fieldName);
                    }
                }

                objectNode = objectNode.remove(toRemove);
            }

            nodes.add(objectNode);
        }

        return nodes;
    }
}
