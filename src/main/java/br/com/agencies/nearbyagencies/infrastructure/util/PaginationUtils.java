package br.com.agencies.nearbyagencies.infrastructure.util;

import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.DecodePageReferenceException;
import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.EncodePageReferenceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class PaginationUtils {

    private PaginationUtils() {}

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String encodeMapToBase64(Map<String, String> lastEvaluatedKeyValue) throws EncodePageReferenceException {
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(lastEvaluatedKeyValue);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (JsonProcessingException e) {
            throw new EncodePageReferenceException("Error encoding page reference: " + e.getMessage());
        }
    }

    public static Map<String, AttributeValue> decodeBase64ToMap(String base64) throws DecodePageReferenceException {
        try {
            byte[] bytes = Base64.getDecoder().decode(base64);
            Map<String, String> stringStringMap = objectMapper.readValue(bytes, new TypeReference<Map<String, String>>() {});
            return convertStringMapToAttributeValueMap(stringStringMap);
        } catch (IOException e) {
            throw new DecodePageReferenceException("Error decoding page reference: " + e.getMessage());
        }
    }

    private static Map<String, AttributeValue> convertStringMapToAttributeValueMap(Map<String, String> stringStringMap) {
        Map<String, AttributeValue> attributeValueMap = new HashMap<>();
        for (Map.Entry<String, String> entry : stringStringMap.entrySet()) {
            attributeValueMap.put(entry.getKey(), AttributeValue.builder().s(entry.getValue()).build());
        }
        return attributeValueMap;
    }
}
