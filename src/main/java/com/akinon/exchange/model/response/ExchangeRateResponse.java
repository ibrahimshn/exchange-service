package com.akinon.exchange.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
public class ExchangeRateResponse {
    private Map<String, BigDecimal> exchangeRates;
}
