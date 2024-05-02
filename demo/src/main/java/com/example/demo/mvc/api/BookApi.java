package com.example.demo.mvc.api;

import com.example.demo.mvc.api.swagger.BookApiSwagger;
import com.example.demo.mvc.dto.BookDto;
import com.example.demo.mvc.dto.BookMapper;
import com.example.demo.mvc.service.BookServiceV2;
import com.example.demo.mvc.service.BookServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("mvc/book")
@RequiredArgsConstructor
public class BookApi implements BookApiSwagger {

    private final BookServiceV1 bookServiceV1;
    private final BookServiceV2 bookServiceV2;
    private final BookMapper bookMapper;


    @PostMapping("/v1")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono createV1(@RequestBody Mono<BookDto.Post> bookCreate){

        return bookCreate.flatMap(book -> {
            log.info("date {}:::{}",book.getCreatedAt(), book.getLastModifiedAt());
            return bookServiceV1.createV1(book);
        }).doOnSubscribe(subscription -> {
            log.info("Inside subscription");
        });
    }

    @PostMapping("/v2")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono createV2(@RequestBody Mono<BookDto.Post> bookCreate){
        return bookCreate.flatMap(book -> {
            log.info("date {}:::{}",book.getCreatedAt(), book.getLastModifiedAt());
            return bookServiceV2.saveBookV1(book);
        }).doOnSubscribe(subscription -> {
            log.info("Inside subscription");
        });
    }

    @GetMapping("/v1/{book-id}")
    public Mono getBookV1(@PathVariable("book-id") long bookId){
        log.info(Thread.currentThread().getName());
        return bookServiceV1.find(bookId);
    }
    @GetMapping("/v2/{book-id}")
    public Mono getBookV2(@PathVariable("book-id") long bookId){
        log.info(Thread.currentThread().getName());
        return bookServiceV2.findBook(bookId);
    }
}
