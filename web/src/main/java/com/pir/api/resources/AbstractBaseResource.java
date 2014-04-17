package com.pir.api.resources;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pir.api.dto.Response;
import com.pir.util.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractBaseResource {

    private final Logger logger = LoggerFactory.getLogger(AbstractBaseResource.class);

    protected static final String AUTH_TOKEN_HEADER = "auth";

    protected static final ObjectMapper OBJECT_MAPPER;

    protected static final int DEFAULT_PAGE_SIZE = 10;

    static {
        OBJECT_MAPPER = SerializationUtils.createObjectMapper();
        // turn off default view inclusion so that only properties marked with the API view get serialized and sent over to the app
        OBJECT_MAPPER.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    }

    protected ObjectNode getObjectNode(Response.Status status, String message) {
        ObjectNode node = OBJECT_MAPPER.createObjectNode();
        node.put("status", status.name());
        node.put("message", message);
        return node;
    }

    protected String getErrorJsonString(String errormsg) {
        return getObjectNode(Response.Status.NOK, errormsg).toString();
    }

    protected String getOkJsonString(String msg) {
        return getObjectNode(Response.Status.OK, msg).toString();
    }
}