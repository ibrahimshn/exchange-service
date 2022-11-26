package com.akinon.exchange.client;

import com.akinon.exchange.model.response.FixerExchangeRateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FixerWebClient {

    private final WebClient webClientFixer;

    public FixerExchangeRateResponse getExchangeRateFixer(String sourceCurrency, List<String> targetCurrency) {
        return webClientFixer.get()
                .uri(uriBuilder -> uriBuilder.path("/latest")
                        .queryParam("base", sourceCurrency)
                        .queryParam("symbols", formatSymbols(targetCurrency))
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatus::is5xxServerError, ClientResponse::createException)
                .bodyToMono(FixerExchangeRateResponse.class)
                .block();
    }

    private String formatSymbols(List<String> targetCurrency) {
        StringBuilder stringBuilder = new StringBuilder();
        targetCurrency.forEach(currency -> stringBuilder.append(currency).append(","));
        return  stringBuilder.toString();
    }

}
