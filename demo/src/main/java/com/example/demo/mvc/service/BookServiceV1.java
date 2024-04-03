package com.example.demo.mvc.service;

import com.example.demo.mvc.dto.BookDto;
import com.example.demo.mvc.dto.BookMapper;
import com.example.demo.mvc.entity.Book;
import com.example.demo.mvc.utils.CustomBeanUtils;
import com.example.demo.common.exception.BusinessLogicException;
import com.example.demo.common.exception.ExceptionCode;
import jakarta.validation.constraints.Positive;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import static org.springframework.data.relational.core.query.Criteria.where;
import static org.springframework.data.relational.core.query.Query.query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.List;

@Slf4j
@Service("bookServiceV5")
@RequiredArgsConstructor
public class BookServiceV1 {
    private final @NonNull R2dbcEntityTemplate template;
    private final @NonNull CustomBeanUtils<Book> beanUtils;
    private final BookMapper bookMapper;

    public Mono<Book> createV1(BookDto.Post dto){
        return verifyExistIsbn(dto.getIsbn())
                .then(template.insert(bookMapper.bookPostToBook(dto)));
    }
    public Mono<Book> createV2(BookDto.Post dto){
        return template.insert(bookMapper.bookPostToBook(dto));
    }

    private Mono<Void> verifyExistIsbn(String isbn) {
        return template.selectOne(query(where("ISBN").is(isbn)), Book.class)
                .flatMap(findBook -> {
                    if (findBook != null){
                        return Mono.error(new BusinessLogicException(
                                ExceptionCode.BOOK_EXISTS));
                    }
                    return Mono.empty();
                });
    }
    
    public Mono<Book> update(Book book){
        return findVerifiedBook(book.getBookId())
                .map(findBook -> beanUtils.copyNonNullProperties(book,findBook))
                .flatMap(updatingBook -> template.update(updatingBook));
    }

    public Mono<Book> find(long bookId){
        return findVerifiedBook(bookId);
    }
    private Mono<Book> findVerifiedBook(long bookId) {
        return template.selectOne(query(where("BOOK_ID").is(bookId)), Book.class)
                .switchIfEmpty(Mono.error(new BusinessLogicException(ExceptionCode.BOOK_NOT_FOUND)));
    }

    public Mono<List<Book>> findBooks(@Positive long page, @Positive long size) {
        return template
                .select(Book.class)
                .count()
                .flatMap(total -> {
                    Tuple2<Long, Long> skipAndTake = getSkipAndTake(total, page, size);
                    return template
                            .select(Book.class)
                            .all()
                            .skip(skipAndTake.getT1())
                            .take(skipAndTake.getT2())
                            .collectSortedList((Book b1, Book b2) -> (int) (b2.getBookId()-b1.getBookId()));
                });
    }

    private Tuple2<Long, Long> getSkipAndTake(Long total, long movePage, long size) {
        long totalPages = (long) Math.ceil((double) total/size);
        long page = movePage > totalPages ? totalPages : movePage;
        long skip = total - (page * size) < 0 ? 0 : total - (page * size);
        long take = total - (page * size) < 0 ? total - ((page-1)*size) : size;

        return Tuples.of(skip, take);
    }

    public Flux<Book> streamingBooks(){
        return template
                .select(Book.class)
                .all()
                .delayElements(Duration.ofSeconds(2L));
    }

}
