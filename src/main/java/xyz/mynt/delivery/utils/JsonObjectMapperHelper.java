
package xyz.mynt.delivery.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@UtilityClass
public class JsonObjectMapperHelper {

    public ObjectMapper createObjectMapper() {

        return new ObjectMapper()
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
            .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    public JsonNode toJsonNode(String str) {
        JsonNode jsonStr = null;
        try {
            jsonStr = createObjectMapper().readTree(str);
        } catch (IOException e) {
            log.error("error when write object to json : {}", str, e);
            return null;
        }

        log.debug("\nsource object : {}\nconverted json : {}", str, jsonStr);

        return jsonStr;
    }

    public String findValue(JsonNode root, String keys) {

        String[] key = StringUtils.split(keys, ".", 2);
        JsonNode node = root.findValue(key[0]);

        if (key.length > 1) {
            return node == null ? null : findValue(node, key[1]);
        }

        return node == null ? null : node.asText();

    }

}
