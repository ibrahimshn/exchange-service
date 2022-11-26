package com.akinon.exchange.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ExchangeLog {
    @Id
    @Column(nullable = false)
    private String transactionId;
    private Date startTime;
    private Date endTime;
    private BigDecimal sourceAmount;
    private String sourceCurrency;
    private String response;
}
