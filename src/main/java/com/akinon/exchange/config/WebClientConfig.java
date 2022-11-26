package com.akinon.exchange.config;

import io.netty.handler.logging.LogLevel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.transport.logging.AdvancedByteBufFormat;

@Configuration
public class WebClientConfig {

    @Value("${fixer.api.baseUrl}")
    private String fixerBaseUrl;

    @Value("${fixer.api.apiKey}")
    private String fixerApiKey;

    @Bean(name = "webClientFixer")
    public WebClient fixerWebClient() {
        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(buildHttpClient()))
                .baseUrl(fixerBaseUrl)
                .defaultHeader("apiKey", fixerApiKey)
                .build();
    }

    private HttpClient buildHttpClient() {
        return HttpClient
                .create()
                .wiretap("reactor.netty.http.client.HttpClient",
                        LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL);
    }

}
