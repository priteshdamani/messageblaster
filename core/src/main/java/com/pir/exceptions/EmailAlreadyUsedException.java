package com.pir.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/8/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailAlreadyUsedException extends GenericException {

    public EmailAlreadyUsedException(String message) {
        super(message);
    }


}
