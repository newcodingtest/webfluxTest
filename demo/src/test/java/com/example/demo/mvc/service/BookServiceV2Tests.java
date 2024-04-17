package com.example.demo.mvc.service;

import com.example.demo.mvc.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class BookServiceV2Tests {
    @Autowired
    private BookServiceV2 bookServiceV2;

    @Test
    public void createTest(){
//        {
//            "bookId": 22,
//                "titleKorean": "yjy1",
//                "titleEnglish": "yjy",
//                "description": "yjy",
//                "author": "yjy",
//                "isbn": "111-11-1111-111-222",
//                "publishDate": "2024-04-16"
//        }
        //given
        Book book = new Book();
        book.setTitleKorean("yjy");
        book.setTitleEnglish("yjy");
        book.setDescription("yjy");
        book.setAuthor("yjy");
        book.setIsbn("111-11-1111-111-222");
        book.setPublishDate("2024-04-16");
        book.setCreatedAt(LocalDateTime.now());
        book.setLastModifiedAt(LocalDateTime.now());

        //when
//        bookServiceV2.saveBookV2(book)
//                .subscribe(result -> {
//                    System.out.println("result = " + result.to);
//                });
        //then
    }
}
