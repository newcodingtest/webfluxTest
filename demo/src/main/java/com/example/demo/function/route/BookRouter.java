package com.example.demo.function.route;

import com.example.demo.function.handler.BookHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class BookRouter {

    @Bean
    public RouterFunction<?> routeBook(BookHandler handler){
        return route()
                .POST("/func/v1", accept(APPLICATION_JSON), handler::createV1)
                .POST("/func/v2", accept(APPLICATION_JSON), handler::createV2)
                .POST("/func/v3", accept(APPLICATION_JSON), handler::createV3)
                //.PATCH()
                .GET("/func/v1/{book-id}", handler::get)
                .GET("/func/v2/{book-id}", handler::get)
                .build();
    }
}
