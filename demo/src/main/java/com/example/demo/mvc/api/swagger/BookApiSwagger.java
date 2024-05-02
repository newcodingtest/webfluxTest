package com.example.demo.mvc.api.swagger;

import com.example.demo.mvc.dto.BookDto;
import com.example.demo.mvc.entity.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Tag(name  = "테스트")
public interface BookApiSwagger {


    @Operation(summary = "R2dbcEntityTemplate 사용", description = "R2dbcEntityTemplate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success",
                        content = {@Content(schema = @Schema(implementation = Book.class ))}),
            @ApiResponse(responseCode = "400", description = "Not Found"),
    })
    Mono createV1(@Parameter(name = "bookCreate", description = "등록 할 정보", example = "3", required = true) Mono<BookDto.Post> bookCreate);
    Mono createV2(Mono<BookDto.Post> bookCreate);
    Mono getBookV1(long bookId);
    Mono getBookV2(long bookId);
}
