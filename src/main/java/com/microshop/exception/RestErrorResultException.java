package com.microshop.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by:   Lijian
 * Created on:   2015/3/4
 * Descriptions:
 */
public class RestErrorResultException extends RuntimeException {

    private HttpStatus httpStatus;

    private ErrorResult errorResult;


    public RestErrorResultException() {
        this(HttpStatus.INTERNAL_SERVER_ERROR); //set default status to 500
    }

    public RestErrorResultException(ErrorResult errorResult) {
        this(HttpStatus.INTERNAL_SERVER_ERROR, errorResult); //set default status to 500
    }

    public RestErrorResultException(HttpStatus httpStatus){
        super();
        this.httpStatus = httpStatus;
    }

    public RestErrorResultException(HttpStatus httpStatus, ErrorResult errorResult){
        super(errorResult.toString());
        this.errorResult = errorResult;
        this.httpStatus = httpStatus;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }



}
