package com.example.demo.function.handler;

import com.example.demo.mvc.dto.BookDto;
import com.example.demo.mvc.dto.BookMapper;
import com.example.demo.common.exception.BusinessLogicException;
import com.example.demo.common.exception.ErrorResponse;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.mvc.service.BookServiceV1;
import com.example.demo.mvc.service.BookServiceV2;
import com.example.demo.mvc.utils.BookValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;


@RequiredArgsConstructor
@Component
public class BookHandler {
    private final BookMapper bookMapper;
    private final BookServiceV1 bookServiceV1;
    private final BookServiceV2 bookServiceV2;
    private final BookValidator validator;

    public Mono<ServerResponse> createV1(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookDto.Post.class)
                .doOnNext(post -> validator.validate(post))
                .flatMap(post -> bookServiceV1.createV1(post))
                .flatMap(book -> ServerResponse.created(URI.create("/func/v1/"+book.getBookId())).build())
                .onErrorResume(BusinessLogicException.class, error ->
                        ServerResponse.badRequest()
                                .bodyValue(ErrorResponse.of(LocalDateTime.now(), error.getExceptionCode().getMessage())));
    }

    public Mono<ServerResponse> createV2(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookDto.Post.class)
                .doOnNext(post -> validator.validate(post))
                .flatMap(post -> bookServiceV2.saveBookV1(post))
                .flatMap(book -> ServerResponse.created(URI.create("/func/v2/"+book.getBookId())).build())
                .onErrorResume(BusinessLogicException.class, error ->
                        ServerResponse.badRequest()
                                .bodyValue(ErrorResponse.of(LocalDateTime.now(), error.getExceptionCode().getMessage())));
    }

    /**
     * globalExceptionHandler 적용 이후 onErrorResume코드 필요없어짐
     * */
    public Mono<ServerResponse> createV3(ServerRequest serverRequest){
        return serverRequest.bodyToMono(BookDto.Post.class)
                .doOnNext(post -> validator.validate(post))
                .flatMap(post -> bookServiceV2.saveBookV1(post))
                .flatMap(book -> ServerResponse.created(URI.create("/func/v2/"+book.getBookId())).build());
    }

    public Mono<ServerResponse> get(ServerRequest serverRequest){
        long bookId = Long.valueOf(serverRequest.pathVariable("book-id"));

        return bookServiceV2.findBook(bookId)
                            .flatMap(book -> ServerResponse
                                    .ok()
                                    .bodyValue(bookMapper.bookToResponse(book))
                                    .switchIfEmpty(ServerResponse.notFound().build()));
    }

}
