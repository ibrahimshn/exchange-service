package com.akinon.exchange.service;

import com.akinon.exchange.client.FixerWebClient;
import com.akinon.exchange.constants.CurrencyCode;
import com.akinon.exchange.exception.InvalidCurrencyCodeException;
import com.akinon.exchange.exception.InvalidDateException;
import com.akinon.exchange.exception.TransactionIdNotFoundException;
import com.akinon.exchange.model.entity.ExchangeLog;
import com.akinon.exchange.model.event.ExchangeLogEvent;
import com.akinon.exchange.model.response.ExchangeAmountResponse;
import com.akinon.exchange.model.response.ExchangeConversionResponse;
import com.akinon.exchange.model.response.ExchangeRateResponse;
import com.akinon.exchange.model.response.FixerExchangeRateResponse;
import com.akinon.exchange.publisher.ExchangeLogPublisher;
import com.akinon.exchange.repository.ExchangeLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ExchangeServiceImpl implements ExchangeService {

    private final FixerWebClient fixerWebClient;
    private final ExchangeLogPublisher exchangeLogPublisher;
    private final ExchangeLogRepository exchangeLogRepository;

    @Override
    public ExchangeRateResponse getExchangeRates(String sourceCurrency, List<String> targetCurrencyList) {
        validateExchangeRequest(sourceCurrency, targetCurrencyList);
        FixerExchangeRateResponse fixerExchangeRateResponse = fixerWebClient.getExchangeRateFixer(sourceCurrency, targetCurrencyList);
        return ExchangeRateResponse.builder()
                .exchangeRates(fixerExchangeRateResponse.getRates())
                .build();
    }

    @Override
    public ExchangeAmountResponse getCalculatedExchangeAmount(BigDecimal sourceAmount, String sourceCurrency, List<String> targetCurrencyList) {
        validateExchangeRequest(sourceCurrency, targetCurrencyList);
        Date startTime = Date.from(Instant.now());
        FixerExchangeRateResponse fixerExchangeRateResponse = fixerWebClient.getExchangeRateFixer(sourceCurrency, targetCurrencyList);
        Date endTime = Date.from(Instant.now());
        ExchangeAmountResponse exchangeAmountResponse = buildExchangeAmountResponse(fixerExchangeRateResponse, sourceAmount);

        exchangeLogPublisher.publishExhangeLogEvent(ExchangeLogEvent.builder()
                .exchangeAmountResponse(exchangeAmountResponse)
                .startTime(startTime)
                .endTime(endTime)
                .sourceAmount(sourceAmount)
                .sourceCurrency(sourceCurrency)
                .build());

        return exchangeAmountResponse;
    }

    @Override
    public ExchangeConversionResponse getConversionsByTransactionId(String transactionId) {
        ExchangeLog exchangeLog = exchangeLogRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionIdNotFoundException("Transaction id : " + transactionId + " not found!"));
        return ExchangeConversionResponse.builder()
                .transactionId(exchangeLog.getTransactionId())
                .responseBody(exchangeLog.getResponse())
                .transactionTime(exchangeLog.getStartTime())
                .build();
    }

    @Override
    public List<ExchangeConversionResponse> getConversionsByStartAndEndTime(Date fromDate, Date toDate) {
        validateDates(fromDate, toDate);
        List<ExchangeLog> exchangeLogs = exchangeLogRepository.findExchangeLogsByStartTimeAfterAndEndTimeBefore(fromDate, toDate);
        return exchangeLogs.stream()
                .map(exchangeLog ->
                        ExchangeConversionResponse.builder()
                                .transactionId(exchangeLog.getTransactionId())
                                .responseBody(exchangeLog.getResponse())
                                .transactionTime(exchangeLog.getStartTime())
                                .build())
                .collect(Collectors.toList());
    }

    private ExchangeAmountResponse buildExchangeAmountResponse(FixerExchangeRateResponse fixerExchangeRateResponse, BigDecimal amount) {
        Map<String, BigDecimal> calculatedExchangeRateAmounts = calculateAmountsForEachTargetCurrency(fixerExchangeRateResponse.getRates(), amount);
        return ExchangeAmountResponse.builder()
                .transactionId(UUID.randomUUID().toString())
                .exchangeAmounts(calculatedExchangeRateAmounts)
                .build();
    }

    private Map<String, BigDecimal> calculateAmountsForEachTargetCurrency(Map<String, BigDecimal> rateExchangeMap, BigDecimal amount) {
        Map<String, BigDecimal> calculatedExchangeRateMaps = new HashMap<>();
        for (Map.Entry<String, BigDecimal> entrySetExchangeRates : rateExchangeMap.entrySet()) {
            calculatedExchangeRateMaps.put(
                    entrySetExchangeRates.getKey(),
                    entrySetExchangeRates.getValue().multiply(amount)
            );
        }
        return calculatedExchangeRateMaps;
    }

    private void validateExchangeRequest(String sourceCurrency, List<String> targetCurrencyList) {
        if (!CurrencyCode.currencyCodes.contains(sourceCurrency)) {
            throw new InvalidCurrencyCodeException("Invalid source currency: " + sourceCurrency);
        }
        for (String targetCurrency : targetCurrencyList) {
            if (!CurrencyCode.currencyCodes.contains(targetCurrency)) {
                throw new InvalidCurrencyCodeException("Invalid target currency: " + targetCurrency);
            }
        }
    }

    private void validateDates(Date startDate, Date endDate) {
        if (Objects.isNull(startDate) || Objects.isNull(endDate)) {
            throw new InvalidDateException("Date parameter cannot be null!");
        }

        if(startDate.after(endDate)) {
            throw new InvalidDateException("End date cannot be earlier than start date!");
        }
    }

}
