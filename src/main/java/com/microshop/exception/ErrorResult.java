package com.microshop.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by yan on 12/8/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResult {

    @JsonProperty("code")
    private int code;

    @JsonProperty("message")
    private String message;

    public ErrorResult() {
    }

    public ErrorResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return String.format("{code: %d, message: %s}", code, message);
    }

}
