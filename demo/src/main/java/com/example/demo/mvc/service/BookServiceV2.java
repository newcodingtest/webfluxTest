package com.example.demo.mvc.service;

import com.example.demo.mvc.dto.BookDto;
import com.example.demo.mvc.dto.BookMapper;
import com.example.demo.mvc.entity.Book;
import com.example.demo.common.exception.BusinessLogicException;
import com.example.demo.common.exception.ExceptionCode;
import com.example.demo.mvc.repository.BookRepository;
import com.example.demo.mvc.utils.CustomBeanUtils;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service("book")
@RequiredArgsConstructor
public class BookServiceV2 {
    private final @NonNull BookRepository bookRepository;
    private final @NonNull CustomBeanUtils<Book> beanUtils;
    private final BookMapper bookMapper;

    public Mono<Book> saveBookV1(BookDto.Post dto) {
        return verifyExistIsbn(dto.getIsbn())
                .then(bookRepository.save(bookMapper.bookPostToBook(dto)))
                .doOnSuccess(result -> log.info("result: {}", result));
    }

    public Mono<Book> saveBookV2(BookDto.Post dto) {
        log.info("hi2");
        return bookRepository.save(bookMapper.bookPostToBook(dto));
    }

    public Mono<Book> updateBook(Book book) {
        return findVerifiedBook(book.getBookId())
                .map(findBook -> beanUtils.copyNonNullProperties(book, findBook))
                .flatMap(updatingBook -> bookRepository.save(updatingBook));
    }

    public Mono<Book> findBook(long bookId) {
        return findVerifiedBook(bookId);
    }

    public Mono<List<Book>> findBooks(@Positive int page,
                                      @Positive int size) {
        return bookRepository
                .findAllBy(PageRequest.of(page - 1, size,
                        Sort.by("memberId").descending()))
                .collectList();
    }

    private Mono<Void> verifyExistIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .flatMap(findBook -> {
                    if (findBook != null) {
                        return Mono.error(new BusinessLogicException(
                                ExceptionCode.BOOK_EXISTS));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Book> findVerifiedBook(long bookId) {
        return bookRepository
                .findById(bookId)
                .switchIfEmpty(Mono.error(new BusinessLogicException(
                        ExceptionCode.BOOK_NOT_FOUND)));
    }
}
