package com.akinon.exchange.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class FixerExchangeRateResponse {
    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, BigDecimal> rates;
}
