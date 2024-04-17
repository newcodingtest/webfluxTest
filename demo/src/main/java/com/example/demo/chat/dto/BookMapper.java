package com.example.demo.chat.dto;

import com.example.demo.chat.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookMapper {
    @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(source = "lastModifiedAt", target = "lastModifiedAt", dateFormat = "yyyy-MM-dd HH:mm:ss")
    Book bookPosttoBook(BookDto.Post book);
    Book bookPatchtoBook(BookDto.Patch book);
    BookDto.Response bookToResponse(Book book);
    List<BookDto.Response> booksToResponse(List<Book> book);
}
