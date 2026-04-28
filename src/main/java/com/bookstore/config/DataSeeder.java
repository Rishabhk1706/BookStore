package com.bookstore.config;

import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
  @Bean
  CommandLineRunner seedBooks(BookRepository bookRepository) {
    return args -> {
      if (bookRepository.count() > 0) {
        List<Book> existingBooks = bookRepository.findAll();
        boolean needsUpgrade = existingBooks.stream().anyMatch(book -> book.getImageUrl() == null || book.getRating() == 0);
        if (needsUpgrade) {
          int index = 1;
          for (Book book : existingBooks) {
            if (book.getImageUrl() == null) {
              book.setImageUrl("https://picsum.photos/seed/bookstore-" + index + "/320/420");
            }
            if (book.getRating() == 0) {
              book.setRating(4.0 + (index % 8) * 0.1);
            }
            if (book.getReviewCount() == 0) {
              book.setReviewCount(18 + index * 5);
            }
            index++;
          }
          bookRepository.saveAll(existingBooks);
        }
        return;
      }

      bookRepository.saveAll(List.of(
          new Book("Atomic Habits", "James Clear", 499.0, "Self-Help", 25,
              "Practical strategies for building good habits and breaking bad ones.",
              "https://picsum.photos/seed/bookstore-1/320/420", 4.8, 128),
          new Book("The Alchemist", "Paulo Coelho", 299.0, "Fiction", 18,
              "A philosophical novel about following your dreams.",
              "https://picsum.photos/seed/bookstore-2/320/420", 4.6, 96),
          new Book("Sapiens", "Yuval Noah Harari", 699.0, "History", 12,
              "A brief history of humankind from early humans to modern societies.",
              "https://picsum.photos/seed/bookstore-3/320/420", 4.7, 142),
          new Book("Clean Code", "Robert C. Martin", 799.0, "Science", 15,
              "A guide to writing readable, maintainable, and professional code.",
              "https://picsum.photos/seed/bookstore-4/320/420", 4.9, 164),
          new Book("Deep Work", "Cal Newport", 450.0, "Self-Help", 20,
              "A framework for focused success in a distracted world.",
              "https://picsum.photos/seed/bookstore-5/320/420", 4.5, 84),
          new Book("1984", "George Orwell", 350.0, "Fiction", 30,
              "A dystopian classic exploring surveillance and authoritarianism.",
              "https://picsum.photos/seed/bookstore-6/320/420", 4.7, 121),
          new Book("The Lean Startup", "Eric Ries", 599.0, "Non-Fiction", 14,
              "Methods for building products through continuous innovation and feedback.",
              "https://picsum.photos/seed/bookstore-7/320/420", 4.4, 73),
          new Book("A Brief History of Time", "Stephen Hawking", 550.0, "Science", 10,
              "An accessible introduction to cosmology, black holes, and the universe.",
              "https://picsum.photos/seed/bookstore-8/320/420", 4.6, 89),
          new Book("Thinking, Fast and Slow", "Daniel Kahneman", 650.0, "Non-Fiction", 11,
              "Insights into how humans think, judge, and make decisions.",
              "https://picsum.photos/seed/bookstore-9/320/420", 4.7, 111),
          new Book("Rich Dad Poor Dad", "Robert T. Kiyosaki", 399.0, "Self-Help", 28,
              "A popular personal finance book about mindset and money.",
              "https://picsum.photos/seed/bookstore-10/320/420", 4.3, 156),
          new Book("The Psychology of Money", "Morgan Housel", 525.0, "Non-Fiction", 24,
              "Short stories about how people think and behave with money.",
              "https://picsum.photos/seed/bookstore-11/320/420", 4.8, 134),
          new Book("To Kill a Mockingbird", "Harper Lee", 320.0, "Fiction", 16,
              "A classic novel on justice, empathy, and moral growth.",
              "https://picsum.photos/seed/bookstore-12/320/420", 4.8, 145),
          new Book("Ikigai", "Hector Garcia and Francesc Miralles", 375.0, "Self-Help", 21,
              "Japanese ideas for living a long and meaningful life.",
              "https://picsum.photos/seed/bookstore-13/320/420", 4.4, 92),
          new Book("The Gene", "Siddhartha Mukherjee", 720.0, "Science", 9,
              "A history of the gene and the science of heredity.",
              "https://picsum.photos/seed/bookstore-14/320/420", 4.5, 61),
          new Book("Guns, Germs, and Steel", "Jared Diamond", 680.0, "History", 13,
              "An explanation of how geography shaped human societies.",
              "https://picsum.photos/seed/bookstore-15/320/420", 4.6, 78)));
    };
  }
}
