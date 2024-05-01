package com.example.demo.sse;

import com.example.demo.mvc.entity.Book;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Validated
@Slf4j
@Service
@RequiredArgsConstructor
public class BookSseService {
    private final @NonNull  R2dbcEntityTemplate template;

    public Flux<Book> streamingBooks() {
        return template
                .select(Book.class)
                .all()
                .delayElements(Duration.ofSeconds(2L));
    }
}
