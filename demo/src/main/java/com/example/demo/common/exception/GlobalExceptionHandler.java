package com.example.demo.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Order(-2)
@RequiredArgsConstructor
@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {
    private final ObjectMapper om;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        return handleException(exchange, ex);
    }

    private Mono<Void> handleException(ServerWebExchange serverWebExchange, Throwable throwable) {
        log.info("전역 핸들러가 잡았어");
        
        ErrorResponse errorResponse = null;
        DataBuffer dataBuffer = null;

        DataBufferFactory bufferFactory =
                            serverWebExchange.getResponse().bufferFactory();
        serverWebExchange.getResponse().getHeaders()
                                        .setContentType(MediaType.APPLICATION_JSON);

        if (throwable instanceof BusinessLogicException) {
            BusinessLogicException ex = (BusinessLogicException) throwable;
            ExceptionCode exceptionCode = ex.getExceptionCode();
            errorResponse = ErrorResponse.of(LocalDateTime.now(), ex.getExceptionCode().getMessage());

            serverWebExchange.getResponse()
                    .setStatusCode(HttpStatus.valueOf(exceptionCode.getStatus()));
        } else if (throwable instanceof ResponseStatusException) {
            ResponseStatusException ex = (ResponseStatusException) throwable;
            errorResponse = ErrorResponse.of(LocalDateTime.now(), ex.getReason());

            serverWebExchange.getResponse()
                    .setStatusCode(HttpStatus.valueOf(ex.getStatusCode().value()));
        } else {
            errorResponse = ErrorResponse.of(LocalDateTime.now(),
                                            throwable.getMessage());

            serverWebExchange.getResponse()
                    .setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        log.info("errorResponse :: {}, {} ", errorResponse.getReason(), errorResponse.getErrorDT());
        try {
            dataBuffer = bufferFactory.wrap(om.writeValueAsBytes(errorResponse));
        }  catch (JsonProcessingException e) {
            bufferFactory.wrap("".getBytes());
        }

        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
