package com.akinon.exchange.exception;

import com.akinon.exchange.common.AppException;

public class TransactionIdNotFoundException extends AppException {
    public TransactionIdNotFoundException(String message) {
        super(message);
    }
}
