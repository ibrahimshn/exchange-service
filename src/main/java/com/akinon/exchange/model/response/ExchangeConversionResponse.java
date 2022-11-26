package com.akinon.exchange.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ExchangeConversionResponse {
    private String transactionId;
    private String responseBody;
    private Date transactionTime;
}
