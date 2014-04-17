package com.pir.dto;

import java.io.Serializable;

/**
 * Created by pritesh on 12/11/13.
 */
public class ChargeAttemptResultDto implements Serializable{
    private boolean success;
    private String message;

    public ChargeAttemptResultDto(boolean success, String message) {

        this.success = success;
        this.message = message;
    }

    public ChargeAttemptResultDto() {
        this.success = true;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
