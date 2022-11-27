package com.akinon.exchange.listener;

import com.akinon.exchange.model.entity.ExchangeLog;
import com.akinon.exchange.model.event.ExchangeLogEvent;
import com.akinon.exchange.repository.ExchangeLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@Slf4j
@EnableAsync
@RequiredArgsConstructor
@Component
public class ExchangeLogListener {

    private final ExchangeLogRepository exchangeLogRepository;

    @Async
    @EventListener
    public void buildExchangeLogEvent(ExchangeLogEvent exchangeLogEvent) {

        try {
            ExchangeLog exchangeLog = new ExchangeLog();
            exchangeLog.setTransactionId(exchangeLogEvent.getExchangeAmountResponse().getTransactionId());
            exchangeLog.setStartTime(exchangeLogEvent.getStartTime());
            exchangeLog.setEndTime(exchangeLogEvent.getEndTime());
            exchangeLog.setSourceCurrency(exchangeLogEvent.getSourceCurrency());
            exchangeLog.setSourceAmount(exchangeLogEvent.getSourceAmount());
            ObjectMapper mapper = new ObjectMapper();
            exchangeLog.setResponse(mapper.writeValueAsString(exchangeLogEvent.getExchangeAmountResponse()));

            exchangeLogRepository.save(exchangeLog);
        } catch (Exception e) {
            log.error("An error occured while logging exchange event! {}", e);
        }

    }

}
