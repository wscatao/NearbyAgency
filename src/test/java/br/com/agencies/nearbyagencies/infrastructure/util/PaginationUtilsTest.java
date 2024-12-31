package br.com.agencies.nearbyagencies.infrastructure.util;

import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.DecodePageReferenceException;
import br.com.agencies.nearbyagencies.infrastructure.adapters.exception.EncodePageReferenceException;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class PaginationUtilsTest {

    @Test
    void testEncodeMapToBase64() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        try {
            String encoded = PaginationUtils.encodeMapToBase64(map);
            assertNotNull(encoded);
            assertFalse(encoded.isEmpty());
        } catch (EncodePageReferenceException e) {
            fail("Encoding should not throw an exception");
        }
    }

    @Test
    void testDecodeBase64ToMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        try {
            String encoded = PaginationUtils.encodeMapToBase64(map);
            Map<String, AttributeValue> decoded = PaginationUtils.decodeBase64ToMap(encoded);

            assertNotNull(decoded);
            assertEquals(2, decoded.size());
            assertEquals("value1", decoded.get("key1").s());
            assertEquals("value2", decoded.get("key2").s());
        } catch (EncodePageReferenceException | DecodePageReferenceException e) {
            fail("Encoding/Decoding should not throw an exception");
        }
    }

    @Test
    void testDecodeBase64ToMapWithException() {
        String invalidBase64 = "invalidBase64String";

        assertThrows(DecodePageReferenceException.class, () -> {
            PaginationUtils.decodeBase64ToMap(invalidBase64);
        });
    }
}
