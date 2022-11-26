package com.akinon.exchange.publisher;

import com.akinon.exchange.model.event.ExchangeLogEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ExchangeLogPublisher {
    private final ApplicationEventPublisher eventPublisher;

    public void publishExhangeLogEvent(ExchangeLogEvent event) {
        eventPublisher.publishEvent(event);
    }
}
