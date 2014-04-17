package com.pir.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: pritesh
 * Date: 12/8/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvalidVerifyEmailCodeException extends GenericException {

    private Long userId;
    private String code;

    public InvalidVerifyEmailCodeException(Long userId, String code) {
        this.userId = userId;
        this.code = code;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
