package com.bookstore.controller;

import com.bookstore.model.Wishlist;
import com.bookstore.model.WishlistItem;
import com.bookstore.service.WishlistService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
public class WishlistController {
    @Autowired
    private WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<?> getWishlist(@RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        Wishlist wishlist = wishlistService.getWishlist(userId);
        return ResponseEntity.ok(wishlist);
    }

    @PostMapping
    public ResponseEntity<?> addItem(@RequestBody WishlistItem item, @RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        Wishlist wishlist = wishlistService.addItem(userId, item);
        return ResponseEntity.ok(wishlist);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> removeItem(@PathVariable String bookId, @RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        Wishlist wishlist = wishlistService.removeItem(userId, bookId);
        return ResponseEntity.ok(wishlist);
    }
}
