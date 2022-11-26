package com.akinon.exchange.model.event;

import com.akinon.exchange.model.response.ExchangeAmountResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
public class ExchangeLogEvent {
    private ExchangeAmountResponse exchangeAmountResponse;
    private Date startTime;
    private Date endTime;
    private BigDecimal sourceAmount;
    private String sourceCurrency;
}
