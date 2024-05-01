package com.example.demo.sse;

import com.example.demo.mvc.dto.BookDto;
import com.example.demo.mvc.dto.BookMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookSseRouter {
    @Bean
    public RouterFunction<?> routeStreamingBook(BookSseService bookService, BookMapper mapper){
        return route(RequestPredicates.GET("/v11/streaming-books"),
                request -> ServerResponse
                        .ok()
                        .contentType(MediaType.TEXT_EVENT_STREAM)
                        .body(bookService
                                .streamingBooks()
                                .map(book -> mapper.bookToResponse(book))
                                ,
                                BookDto.Response.class));
    }
}
