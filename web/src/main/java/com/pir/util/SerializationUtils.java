package com.pir.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.joda.JodaModule;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class SerializationUtils {

    // objectMapper (with standard default configuration) which can be shared
    private static final ObjectMapper OBJECT_MAPPER = createObjectMapper();

    /**
     * returns a standard objectmapper with some default date handling. use when the returned ObjectMapper won't be
     * customized
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * used for creating objectMappers that'll be customized
     * @return
     */
    public static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // register JodaModule for jodatime de/serialization
        objectMapper.registerModule(new JodaModule());
        objectMapper.setDateFormat(new ISO8601DateFormat());
        return objectMapper;
    }

}

