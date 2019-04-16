package com.surroundinsurance.user.service.platform.common;

import org.springframework.http.HttpStatus;

public class InternalErrorException extends HTTPException {

    private static final long serialVersionUID = 1L;

    public InternalErrorException(String statusCode, Throwable e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusCode, e);
    }

    public InternalErrorException(String statusCode, String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusCode, message);
    }

    public InternalErrorException(String statusCode, String message, Throwable e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusCode, message, e);
    }

    public InternalErrorException(String statusCode, String message, String externalMessage) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusCode, message, externalMessage);
    }

    public InternalErrorException(String statusCode, String message, String externalMessage, Throwable e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, statusCode, message, externalMessage, e);
    }

    public InternalErrorException(Throwable e) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "ERROR", "ERROR", e);
    }

}
