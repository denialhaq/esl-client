package org.freeswitch.esl.client.decoders;

import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonDecoder {

    ObjectMapper objectMapper = new ObjectMapper();

    private static JsonDecoder ourInstance = new JsonDecoder();

    public static JsonDecoder getInstance() {
        return ourInstance;
    }

    private JsonDecoder() {
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
