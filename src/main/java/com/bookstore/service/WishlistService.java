package com.bookstore.service;

import com.bookstore.model.Wishlist;
import com.bookstore.model.WishlistItem;
import com.bookstore.repository.WishlistRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {
    @Autowired
    private WishlistRepository wishlistRepository;

    public Wishlist getOrCreateWishlist(String userId) {
        Optional<Wishlist> existingWishlist = wishlistRepository.findByUserId(userId);
        if (existingWishlist.isPresent()) {
            return existingWishlist.get();
        }
        return wishlistRepository.save(new Wishlist(userId));
    }

    public Wishlist getWishlist(String userId) {
        return getOrCreateWishlist(userId);
    }

    public Wishlist addItem(String userId, WishlistItem item) {
        Wishlist wishlist = getOrCreateWishlist(userId);
        boolean exists = wishlist.getItems().stream().anyMatch(existing -> existing.getBookId().equals(item.getBookId()));
        if (!exists) {
            wishlist.getItems().add(item);
        }
        return wishlistRepository.save(wishlist);
    }

    public Wishlist removeItem(String userId, String bookId) {
        Wishlist wishlist = getOrCreateWishlist(userId);
        wishlist.getItems().removeIf(item -> item.getBookId().equals(bookId));
        return wishlistRepository.save(wishlist);
    }
}
