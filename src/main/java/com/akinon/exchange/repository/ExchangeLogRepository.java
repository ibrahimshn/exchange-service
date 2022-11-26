package com.akinon.exchange.repository;

import com.akinon.exchange.model.entity.ExchangeLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ExchangeLogRepository extends CrudRepository<ExchangeLog, String> {
    List<ExchangeLog> findExchangeLogsByStartTimeAfterAndEndTimeBefore(Date startDate, Date endDate);
}
