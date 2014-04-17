package com.pir.api.dto;

import com.fasterxml.jackson.annotation.JsonView;
import com.pir.util.JsonViews;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/5/13
 * Time: 1:59 PM
 * To change this template use File | Settings | File Templates.
 */

public class Response<T> implements Serializable {

    public enum Status{
        OK,
        NOK
    }

    @JsonView(JsonViews.API.class)
    private Status status;
    @JsonView(JsonViews.API.class)
    private String message;
    @JsonView(JsonViews.API.class)
    private T payload;


    public Response() {
    }

    public Response(Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response(Status status, T payload) {
        this.status = status;
        this.payload = payload;
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
