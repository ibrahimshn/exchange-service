package com.akinon.exchange.model.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
public class AppError {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private final Date timestamp;

    @JsonIgnore
    private HttpStatus httpStatus;
    private int status;
    private String error;
    private String message;

    public AppError() {
        this.timestamp = Date.from(Instant.now());
    }

}
