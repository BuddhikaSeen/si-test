package com.surroundinsurance.user.service.platform.common;

import org.springframework.http.HttpStatus;

public class BadRequestException extends HTTPException {

    private static final long serialVersionUID = 1L;

    public BadRequestException(String statusCode, String message, String externalMessage) {
        super(HttpStatus.BAD_REQUEST, statusCode, message, externalMessage);
    }

    public BadRequestException(String statusCode, String message) {
        super(HttpStatus.BAD_REQUEST, statusCode, message);
    }

    public BadRequestException(String statusCode) {
        super(HttpStatus.BAD_REQUEST, statusCode);
    }

}
