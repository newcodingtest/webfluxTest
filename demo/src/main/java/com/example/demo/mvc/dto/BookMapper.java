package com.example.demo.mvc.dto;

import com.example.demo.mvc.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "lastModifiedAt", target = "lastModifiedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Book bookPostToBook(BookDto.Post book);
    Book bookPatchToBook(BookDto.Patch book);
    BookDto.Response bookToResponse(Book book);
    List<BookDto.Response> booksToResponse(List<Book> book);
}
