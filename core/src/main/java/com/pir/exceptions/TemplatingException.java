package com.pir.exceptions;

/**
 * Created by pritesh on 12/14/13.
 */
public class TemplatingException extends GenericException{
    public TemplatingException(String message, Exception e) {
        super(message,e);
    }
}
