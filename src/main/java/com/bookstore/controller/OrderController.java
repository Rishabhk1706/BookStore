package com.bookstore.controller;

import com.bookstore.model.Order;
import com.bookstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<?> placeOrder(@RequestBody Order order, @RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        order.setUserId(userId);
        Order savedOrder = orderService.placeOrder(order);
        return ResponseEntity.ok(savedOrder);
    }

    @GetMapping
    public ResponseEntity<?> getUserOrders(@RequestAttribute(required = false) String userId) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        List<Order> orders = orderService.getUserOrders(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllOrders(@RequestAttribute(required = false) String role) {
        if (!"ADMIN".equals(role)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only admins can view all orders");
            return ResponseEntity.status(403).body(error);
        }
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable String orderId,
            @RequestAttribute(required = false) String userId,
            @RequestAttribute(required = false) String role) {
        Optional<Order> order = orderService.getOrderById(orderId);
        if (order.isPresent()) {
            if (!"ADMIN".equals(role) && !order.get().getUserId().equals(userId)) {
                Map<String, String> error = new HashMap<>();
                error.put("message", "Unauthorized");
                return ResponseEntity.status(403).body(error);
            }
            return ResponseEntity.ok(order.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> request, @RequestAttribute(required = false) String role) {
        if (!"ADMIN".equals(role)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only admins can update order status");
            return ResponseEntity.status(403).body(error);
        }
        String status = request.get("status");
        Order updatedOrder = orderService.updateOrderStatus(orderId, status);
        if (updatedOrder != null) {
            return ResponseEntity.ok(updatedOrder);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<?> deleteOrder(@PathVariable String orderId, @RequestAttribute(required = false) String role) {
        if (!"ADMIN".equals(role)) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Only admins can delete orders");
            return ResponseEntity.status(403).body(error);
        }
        boolean deleted = orderService.deleteOrder(orderId);
        if (deleted) {
            return ResponseEntity.ok(new HashMap<String, String>() {{
                put("message", "Order deleted successfully");
            }});
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String orderId,
            @RequestAttribute(required = false) String userId,
            @RequestAttribute(required = false) String role) {
        if (userId == null) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Unauthorized");
            return ResponseEntity.status(401).body(error);
        }
        return ResponseEntity.ok(orderService.cancelOrder(orderId, userId, role));
    }
}
