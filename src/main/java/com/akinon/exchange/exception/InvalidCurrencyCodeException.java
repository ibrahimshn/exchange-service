package com.akinon.exchange.exception;

import com.akinon.exchange.common.AppException;

public class InvalidCurrencyCodeException extends AppException {
    public InvalidCurrencyCodeException(String message) {
        super(message);
    }
}
