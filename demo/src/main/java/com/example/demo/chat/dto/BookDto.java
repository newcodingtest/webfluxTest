package com.example.demo.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class BookDto {
    @Getter
    public static class Post {
        private String titleKorean;
        private String titleEnglish;
        private String description;
        private String author;
        private String isbn;
        private String publishDate;
        private String createdAt;
        private String lastModifiedAt;
    }

    @Getter
    public static class Patch {
        @Setter
        private String titleKorean;
        private String titleEnglish;
        private String description;
        private String author;
        private String isbn;
        private String publishDate;
    }

    @Builder
    @Getter
    public static class Response {
        private String titleKorean;
        private String titleEnglish;
        private String description;
        private String author;
        private String isbn;
        private String publishDate;
    }
}
