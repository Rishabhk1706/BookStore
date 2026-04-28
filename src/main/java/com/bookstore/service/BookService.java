package com.bookstore.service;

import com.bookstore.model.Book;
import com.bookstore.exception.ApiException;
import com.bookstore.repository.BookRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
  @Autowired
  private BookRepository bookRepository;

  public Book addBook(Book book) {
    return bookRepository.save(book);
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Optional<Book> getBookById(String id) {
    return bookRepository.findById(id);
  }

  public List<Book> searchByCategory(String category) {
    return bookRepository.findByCategory(category);
  }

  public List<Book> searchByTitle(String title) {
    return bookRepository.findByTitleContainingIgnoreCase(title);
  }

  public List<Book> discoverBooks(String query, String category, String sortBy, int page, int size) {
    List<Book> books = bookRepository.findAll();

    if (query != null && !query.isBlank()) {
      String normalizedQuery = query.trim();
      books = bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(normalizedQuery, normalizedQuery);
    }

    if (category != null && !category.isBlank() && !"all".equalsIgnoreCase(category)) {
      final String normalizedCategory = category.trim();
      books = books.stream()
          .filter(book -> normalizedCategory.equalsIgnoreCase(book.getCategory()))
          .collect(Collectors.toList());
    }

    if ("priceAsc".equalsIgnoreCase(sortBy)) {
      books.sort((first, second) -> Double.compare(first.getPrice(), second.getPrice()));
    } else if ("priceDesc".equalsIgnoreCase(sortBy)) {
      books.sort((first, second) -> Double.compare(second.getPrice(), first.getPrice()));
    } else if ("rating".equalsIgnoreCase(sortBy)) {
      books.sort((first, second) -> Double.compare(second.getRating(), first.getRating()));
    } else {
      books.sort((first, second) -> first.getTitle().compareToIgnoreCase(second.getTitle()));
    }

    int safePage = Math.max(page, 0);
    int safeSize = Math.max(size, 1);
    int start = safePage * safeSize;
    if (start >= books.size()) {
      return List.of();
    }
    int end = Math.min(start + safeSize, books.size());
    return books.subList(start, end);
  }

  public int countDiscoverBooks(String query, String category) {
    return discoverBooks(query, category, "title", 0, Integer.MAX_VALUE).size();
  }

  public Book updateBook(String id, Book book) {
    Optional<Book> existingBook = bookRepository.findById(id);
    if (existingBook.isPresent()) {
      Book bookToUpdate = existingBook.get();
      if (book.getTitle() != null)
        bookToUpdate.setTitle(book.getTitle());
      if (book.getAuthor() != null)
        bookToUpdate.setAuthor(book.getAuthor());
      if (book.getPrice() > 0)
        bookToUpdate.setPrice(book.getPrice());
      if (book.getCategory() != null)
        bookToUpdate.setCategory(book.getCategory());
      if (book.getStock() >= 0)
        bookToUpdate.setStock(book.getStock());
      if (book.getDescription() != null)
        bookToUpdate.setDescription(book.getDescription());
      if (book.getImageUrl() != null)
        bookToUpdate.setImageUrl(book.getImageUrl());
      if (book.getRating() > 0)
        bookToUpdate.setRating(book.getRating());
      if (book.getReviewCount() >= 0)
        bookToUpdate.setReviewCount(book.getReviewCount());
      return bookRepository.save(bookToUpdate);
    }
    return null;
  }

  public boolean deleteBook(String id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
      return true;
    }
    return false;
  }

  public List<Book> getLowStockBooks() {
    return bookRepository.findByStockLessThan(6);
  }

  public Book getRequiredBook(String id) {
    return bookRepository.findById(id).orElseThrow(() -> new ApiException("Book not found"));
  }
}
