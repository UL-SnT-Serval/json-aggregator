package lu.uni.serval.json.aggregator.merger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.*;

public class JsonMerger {
    private JsonMerger() {}

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
            final ObjectNode objectNode = (ObjectNode) jsonNode;

            if(!filter.isEmpty()){
                Set<String> toRemove = new HashSet<>();
                for (Iterator<String> it = objectNode.fieldNames(); it.hasNext(); ) {
                    final String fieldName = it.next();
                    if(filter.contains(fieldName)){
                        toRemove.add(fieldName);
                    }
                }

                objectNode.remove(toRemove);
            }

            nodes.add(objectNode);
        }

        return nodes;
    }
}
