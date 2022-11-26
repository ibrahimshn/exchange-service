package com.akinon.exchange.controller;

import com.akinon.exchange.model.response.ExchangeRateResponse;
import com.akinon.exchange.service.ExchangeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExchangeController.class)
public class ExchangeControllerTest {
    @MockBean
    private ExchangeService exchangeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenValidExchangeRateRequest_whenQueryExchangeRate_thenReturnSuccessfulResponse() throws Exception {
        when(exchangeService.getExchangeRates("USD", Collections.singletonList("TRY")))
                .thenReturn(buildExchangeResponse());
        mockMvc.perform(
                        get("/exchange-api/exchange-rate")
                                .param("sourceCurrency", "USD")
                                .param("targetCurrencyList", "TRY")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void givenInCompleteExchangeRequest_whenQueryExchangeRate_thenThrowException() throws Exception {
        mockMvc.perform(
                        get("/exchange-api/exchange-rate")
                                .param("targetCurrencyList", "TRY")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    private ExchangeRateResponse buildExchangeResponse() {
        Map<String, BigDecimal> exchangeResponseMap  = new HashMap<>() {{
            put("TRY", new BigDecimal(10));
        }};
        return ExchangeRateResponse.builder()
                .exchangeRates(exchangeResponseMap)
                .build();
    }
}
