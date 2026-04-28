package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<?> getAllBooks(@RequestParam(required = false) String query,
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "title") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "9") int size) {
        Map<String, Object> response = new HashMap<>();
        response.put("items", bookService.discoverBooks(query, category, sortBy, page, size));
        response.put("page", page);
        response.put("size", size);
        response.put("totalItems", bookService.countDiscoverBooks(query, category));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookById(@PathVariable String id) {
        Optional<Book> book = bookService.getBookById(id);
        if (book.isPresent()) {
            return ResponseEntity.ok(book.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/search/category/{category}")
    public ResponseEntity<?> searchByCategory(@PathVariable String category) {
        List<Book> books = bookService.searchByCategory(category);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/search/title/{title}")
    public ResponseEntity<?> searchByTitle(@PathVariable String title) {
        List<Book> books = bookService.searchByTitle(title);
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book, @RequestAttribute(required = false) String role) {
        if (!"ADMIN".equals(role)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only admins can add books");
            return ResponseEntity.status(403).body(error);
        }
        Book savedBook = bookService.addBook(book);
        return ResponseEntity.ok(savedBook);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody Book book, @RequestAttribute(required = false) String role) {
        if (!"ADMIN".equals(role)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only admins can update books");
            return ResponseEntity.status(403).body(error);
        }
        Book updatedBook = bookService.updateBook(id, book);
        if (updatedBook != null) {
            return ResponseEntity.ok(updatedBook);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id, @RequestAttribute(required = false) String role) {
        if (!"ADMIN".equals(role)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only admins can delete books");
            return ResponseEntity.status(403).body(error);
        }
        boolean deleted = bookService.deleteBook(id);
        if (deleted) {
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("message", "Book deleted successfully");
            }});
        }
        return ResponseEntity.notFound().build();
    }
}
