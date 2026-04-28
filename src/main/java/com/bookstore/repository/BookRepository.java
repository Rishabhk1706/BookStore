package com.bookstore.repository;

import com.bookstore.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
  List<Book> findByCategory(String category);

  List<Book> findByTitleContainingIgnoreCase(String title);

  List<Book> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);

  List<Book> findByStockLessThan(int stock);

  Optional<Book> findFirstByOrderByRatingDesc();
}
