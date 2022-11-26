package com.akinon.exchange.exception;

import com.akinon.exchange.common.AppException;

public class InvalidDateException extends AppException {
    public InvalidDateException(String message) {
        super(message);
    }
}
