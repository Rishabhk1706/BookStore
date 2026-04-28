package com.bookstore.controller;

import com.bookstore.service.BookService;
import com.bookstore.service.OrderService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private BookService bookService;

    @GetMapping("/stats")
    public ResponseEntity<?> getStats(@RequestAttribute(required = false) String role) {
        if (!"ADMIN".equals(role)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only admins can view dashboard stats");
            return ResponseEntity.status(403).body(error);
        }
        return ResponseEntity.ok(orderService.getDashboardStats(bookService.getLowStockBooks()));
    }
}
