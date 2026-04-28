package com.bookstore.service;

import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.model.Book;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.CartRepository;
import com.bookstore.exception.ApiException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
  @Autowired
  private CartRepository cartRepository;

  @Autowired
  private BookRepository bookRepository;

  public Cart getOrCreateCart(String userId) {
    Optional<Cart> existingCart = cartRepository.findByUserId(userId);
    if (existingCart.isPresent()) {
      return existingCart.get();
    }
    Cart newCart = new Cart(userId);
    return cartRepository.save(newCart);
  }

  public Cart addToCart(String userId, CartItem item) {
    Cart cart = getOrCreateCart(userId);
    Book book = bookRepository.findById(item.getBookId())
        .orElseThrow(() -> new ApiException("Selected book does not exist"));

    if (book.getStock() <= 0) {
      throw new ApiException("This book is currently out of stock");
    }

    item.setTitle(book.getTitle());
    item.setPrice(book.getPrice());

    // Check if item already exists
    Optional<CartItem> existingItem = cart.getItems().stream()
        .filter(i -> i.getBookId().equals(item.getBookId()))
        .findFirst();

    if (existingItem.isPresent()) {
      existingItem.get().setQuantity(existingItem.get().getQuantity() + item.getQuantity());
    } else {
      cart.getItems().add(item);
    }

    return cartRepository.save(cart);
  }

  public Cart removeFromCart(String userId, String bookId) {
    Cart cart = getOrCreateCart(userId);
    cart.getItems().removeIf(item -> item.getBookId().equals(bookId));
    return cartRepository.save(cart);
  }

  public Cart updateQuantity(String userId, String bookId, int quantity) {
    Cart cart = getOrCreateCart(userId);

    Optional<CartItem> item = cart.getItems().stream()
        .filter(i -> i.getBookId().equals(bookId))
        .findFirst();

    if (item.isPresent()) {
      if (quantity <= 0) {
        cart.getItems().removeIf(i -> i.getBookId().equals(bookId));
      } else {
        item.get().setQuantity(quantity);
      }
    }

    return cartRepository.save(cart);
  }

  public Cart clearCart(String userId) {
    Cart cart = getOrCreateCart(userId);
    cart.getItems().clear();
    return cartRepository.save(cart);
  }

  public Cart getCart(String userId) {
    return getOrCreateCart(userId);
  }
}
