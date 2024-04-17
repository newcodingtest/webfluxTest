package com.example.demo.chat.api;

import com.example.demo.chat.dto.BookDto;
import com.example.demo.chat.dto.BookMapper;
import com.example.demo.chat.exception.ErrorResponse;
import com.example.demo.chat.service.BookServiceV1;
import com.example.demo.chat.service.BookServiceV2;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("reactive/book")
@RequiredArgsConstructor
public class BookApi {

    private final BookServiceV1 bookServiceV1;
    private final BookServiceV2 bookServiceV2;
    private final BookMapper bookMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono createV1(@RequestBody Mono<BookDto.Post> bookCreate){

        Mono result = bookCreate.flatMap(book -> {
            return Mono.just(bookServiceV1.createV1(book));
        });
        return result;
    }

    @PostMapping("/v2")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono createV2(@RequestBody Mono<BookDto.Post> bookCreate){
        log.info("hi");
        return bookCreate.flatMap(book -> {
            log.info("date {}:::{}",book.getCreatedAt(), book.getLastModifiedAt());
            return bookServiceV2.saveBookV1(book);
        }).doOnSubscribe(subscription -> {
            log.info("Inside subscription");
        });
    }

    @GetMapping("{book-id}")
    public Mono getBook(@PathVariable("book-id") long bookId){
        log.info(Thread.currentThread().getName());
        return bookServiceV1.find(bookId);
    }
    @GetMapping("/v2/{book-id}")
    public Mono getBookV2(@PathVariable("book-id") long bookId){
        log.info(Thread.currentThread().getName());
        return bookServiceV2.findBook(bookId);
    }
}
