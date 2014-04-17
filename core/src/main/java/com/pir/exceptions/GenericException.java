package com.pir.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/8/13
 * Time: 6:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class GenericException extends Exception {

    public GenericException() {
    }

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Exception e) {
        super(message,e);
    }
}
