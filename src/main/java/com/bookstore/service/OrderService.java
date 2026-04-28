package com.bookstore.service;

import com.bookstore.exception.ApiException;
import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.repository.BookRepository;
import com.bookstore.repository.OrderRepository;
import com.bookstore.repository.UserRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private UserRepository userRepository;

  public Order placeOrder(Order order) {
    if (order.getItems() == null || order.getItems().isEmpty()) {
      throw new ApiException("Cannot place an order with an empty cart");
    }

    double subtotal = 0;
    for (OrderItem item : order.getItems()) {
      Book book = bookRepository.findById(item.getBookId())
          .orElseThrow(() -> new ApiException("A selected book could not be found"));
      if (book.getStock() < item.getQuantity()) {
        throw new ApiException("Not enough stock available for " + book.getTitle());
      }
      item.setTitle(book.getTitle());
      item.setPrice(book.getPrice());
      subtotal += item.getTotal();
      book.setStock(book.getStock() - item.getQuantity());
      bookRepository.save(book);
    }

    double tax = Math.round(subtotal * 0.05 * 100.0) / 100.0;
    order.setSubtotal(subtotal);
    order.setTax(tax);
    order.setTotalPrice(subtotal + tax);
    order.setStatus("PENDING");
    return orderRepository.save(order);
  }

  public List<Order> getUserOrders(String userId) {
    return orderRepository.findByUserId(userId);
  }

  public Optional<Order> getOrderById(String orderId) {
    return orderRepository.findById(orderId);
  }

  public List<Order> getAllOrders() {
    return orderRepository.findAll();
  }

  public Order updateOrderStatus(String orderId, String status) {
    Optional<Order> order = orderRepository.findById(orderId);
    if (order.isPresent()) {
      Order foundOrder = order.get();
      foundOrder.setStatus(status);
      return orderRepository.save(foundOrder);
    }
    return null;
  }

  public boolean deleteOrder(String orderId) {
    if (orderRepository.existsById(orderId)) {
      orderRepository.deleteById(orderId);
      return true;
    }
    return false;
  }

  public Order cancelOrder(String orderId, String userId, String role) {
    Order order = orderRepository.findById(orderId).orElseThrow(() -> new ApiException("Order not found"));
    boolean isAdmin = "ADMIN".equals(role);
    if (!isAdmin && !order.getUserId().equals(userId)) {
      throw new ApiException("You cannot cancel this order");
    }
    if (!"PENDING".equals(order.getStatus()) && !"CONFIRMED".equals(order.getStatus())) {
      throw new ApiException("Only pending or confirmed orders can be cancelled");
    }

    order.setStatus("CANCELLED");
    for (OrderItem item : order.getItems()) {
      bookRepository.findById(item.getBookId()).ifPresent(book -> {
        book.setStock(book.getStock() + item.getQuantity());
        bookRepository.save(book);
      });
    }
    return orderRepository.save(order);
  }

  public Map<String, Object> getDashboardStats(List<Book> lowStockBooks) {
    Map<String, Object> stats = new HashMap<>();
    stats.put("totalUsers", userRepository.count());
    stats.put("totalOrders", orderRepository.count());
    stats.put("pendingOrders", orderRepository.countByStatus("PENDING"));
    stats.put("lowStockBooks", lowStockBooks);
    stats.put("revenue", orderRepository.findAll().stream()
        .filter(order -> !"CANCELLED".equals(order.getStatus()))
        .mapToDouble(Order::getTotalPrice)
        .sum());
    return stats;
  }
}
