package com.akinon.exchange.service;

import com.akinon.exchange.client.FixerWebClient;
import com.akinon.exchange.exception.InvalidCurrencyCodeException;
import com.akinon.exchange.model.response.ExchangeAmountResponse;
import com.akinon.exchange.model.response.ExchangeRateResponse;
import com.akinon.exchange.model.response.FixerExchangeRateResponse;
import com.akinon.exchange.publisher.ExchangeLogPublisher;
import com.akinon.exchange.repository.ExchangeLogRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = ExchangeServiceImpl.class)
public class ExchangeServiceImplTest {

    @Autowired
    ExchangeService exchangeService;

    @MockBean
    FixerWebClient fixerWebClient;

    @MockBean
    ExchangeLogPublisher exchangeLogPublisher;

    @MockBean
    ExchangeLogRepository exchangeLogRepository;


    @Test
    void givenValidExchangeRateSourceCurrency_whenCallExchangeApi_ThenReturnSuccessfulResponse() {
        FixerExchangeRateResponse fixerExchangeRateResponse = buildSuccessFixerExchangeRateResponse();
        Mockito.when(fixerWebClient.getExchangeRateFixer(any(),any()))
                .thenReturn(fixerExchangeRateResponse);
        ExchangeRateResponse exchangeRateResponse = exchangeService.getExchangeRates("USD",
                Collections.singletonList("TRY"));
        assertEquals(1, exchangeRateResponse.getExchangeRates().size());
    }

    @Test
    void givenInValidExchangeRateSourceCurrency_whenCallExchangeApi_ThenThrowInvalidCurrencyCodeException() {
        assertThrows(InvalidCurrencyCodeException.class, () ->
                exchangeService.getExchangeRates("USS", Collections.singletonList("TRY")));
    }

    @Test
    void givenInValidExchangeRateTargetCurrency_whenCallExchangeApi_ThenThrowInvalidCurrencyCodeException() {
        assertThrows(InvalidCurrencyCodeException.class, () ->
                exchangeService.getExchangeRates("USD", Collections.singletonList("TRR")));
    }

    @Test
    void givenExchangeRateAmount_whenCallExchangeApi_ThenReturnSuccessfulResponse() {
        final FixerExchangeRateResponse fixerExchangeRateResponse = buildSuccessFixerExchangeRateResponse();
        Mockito.when(fixerWebClient.getExchangeRateFixer(any(),any()))
                .thenReturn(fixerExchangeRateResponse);
        final ExchangeAmountResponse exchangeAmountResponse =
                exchangeService.getCalculatedExchangeAmount(new BigDecimal(10), "USD", Collections.singletonList("TRY"));
        for (Map.Entry<String, BigDecimal> calculatedEntrySet : exchangeAmountResponse.getExchangeAmounts().entrySet()) {
            assertEquals("TRY", calculatedEntrySet.getKey());
            assertEquals(new BigDecimal(200), calculatedEntrySet.getValue());
        }
    }

    private FixerExchangeRateResponse buildSuccessFixerExchangeRateResponse() {
        Map<String, BigDecimal> exchangeResponseMap  = new HashMap<>() {{
            put("TRY", new BigDecimal(20));
        }};
        FixerExchangeRateResponse fixerExchangeRateResponse = new FixerExchangeRateResponse();
        fixerExchangeRateResponse.setSuccess(true);
        fixerExchangeRateResponse.setDate("2022-11-26");
        fixerExchangeRateResponse.setRates(exchangeResponseMap);
        fixerExchangeRateResponse.setTimestamp(1669502371L);
        return fixerExchangeRateResponse;
    }

}
