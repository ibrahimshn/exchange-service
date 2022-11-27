package com.akinon.exchange.controller;


import com.akinon.exchange.model.response.ExchangeConversionResponse;
import com.akinon.exchange.model.response.ExchangeRateResponse;
import com.akinon.exchange.model.response.ExchangeAmountResponse;
import com.akinon.exchange.service.ExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("exchange-api")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/exchange-rate")
    public ExchangeRateResponse getExchangeRate(@RequestParam @NotBlank String sourceCurrency,
                                                @RequestParam @Size(min = 1) List<String> targetCurrencyList) {
        return exchangeService.getExchangeRates(sourceCurrency, targetCurrencyList);
    }

    @GetMapping("/exchange")
    public ExchangeAmountResponse getExchangeAmount(@RequestParam @NotNull @DecimalMin("0.01") BigDecimal sourceAmount,
                                                    @RequestParam @NotBlank String sourceCurrency,
                                                    @RequestParam @Size(min = 1) List<String> targetCurrencyList) {
        return exchangeService.getCalculatedExchangeAmount(sourceAmount, sourceCurrency, targetCurrencyList);
    }

    @GetMapping("/conversions")
    public List<ExchangeConversionResponse> getExchangeConversions(
            @RequestParam(name = "transactionId", required = false) String transactionId,
            @RequestParam(name = "fromDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date fromDate,
            @RequestParam(name = "toDate", required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date toDate) {

        if(StringUtils.hasText(transactionId)) {
            return Collections.singletonList(exchangeService.getConversionsByTransactionId(transactionId));
        } else {
            return exchangeService.getConversionsByStartAndEndTime(fromDate, toDate);
        }
    }

}
