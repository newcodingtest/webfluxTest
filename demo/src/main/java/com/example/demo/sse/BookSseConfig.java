package com.example.demo.sse;

import com.example.demo.mvc.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@Configuration
public class BookSseConfig {
    @Bean
    public ApplicationRunner streamingBooks(){
        return (ApplicationArguments arguments) -> {
            WebClient webClient = WebClient.create("http://localhost:8080");
            Flux<BookDto.Response> response =
                    webClient
                            .get()
                            .uri("http://localhost:8080/v11/streaming-books")
                            .retrieve()
                            .bodyToFlux(BookDto.Response.class);

            response.subscribe(book -> {
                log.info("book info :: {} ", book);
            },
                    error -> log.error("error :: {} ", error));
        };
    }
}
