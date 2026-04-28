package com.bookstore.controller;

import com.bookstore.model.Cart;
import com.bookstore.model.CartItem;
import com.bookstore.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<?> getCart(@RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        Cart cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItem item, @RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        Cart cart = cartService.addToCart(userId, item);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> removeFromCart(@PathVariable String bookId, @RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        Cart cart = cartService.removeFromCart(userId, bookId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{bookId}/quantity")
    public ResponseEntity<?> updateQuantity(@PathVariable String bookId, @RequestBody Map<String, Integer> request, @RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        int quantity = request.get("quantity");
        Cart cart = cartService.updateQuantity(userId, bookId, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        Cart cart = cartService.clearCart(userId);
        return ResponseEntity.ok(cart);
    }
}
