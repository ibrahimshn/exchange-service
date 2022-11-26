package com.akinon.exchange.service;

import com.akinon.exchange.model.response.ExchangeAmountResponse;
import com.akinon.exchange.model.response.ExchangeConversionResponse;
import com.akinon.exchange.model.response.ExchangeRateResponse;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface ExchangeService {

    ExchangeRateResponse getExchangeRates(String sourceCurrency, List<String> targetCurrencyList);

    ExchangeAmountResponse getCalculatedExchangeAmount(BigDecimal sourceAmount, String sourceCurrency, List<String> targetCurrencyList);

    ExchangeConversionResponse getConversionsByTransactionId(String transactionId);

    List<ExchangeConversionResponse> getConversionsByStartAndEndTime(Date fromDate, Date toDate);
}
