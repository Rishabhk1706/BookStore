package com.bookstore.service;

import com.bookstore.model.User;
import com.bookstore.repository.UserRepository;
import com.bookstore.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private JwtUtil jwtUtil;

  private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  public Map<String, Object> register(String name, String email, String password) {
    Map<String, Object> response = new HashMap<>();

    Optional<User> existingUser = userRepository.findByEmail(email);
    if (existingUser.isPresent()) {
      response.put("success", false);
      response.put("message", "Email already registered");
      return response;
    }

    User user = new User(name, email, passwordEncoder.encode(password), "USER");
    User savedUser = userRepository.save(user);

    response.put("success", true);
    response.put("message", "User registered successfully");
    response.put("userId", savedUser.getId());
    return response;
  }

  public Map<String, Object> login(String email, String password) {
    Map<String, Object> response = new HashMap<>();

    Optional<User> user = userRepository.findByEmail(email);
    if (!user.isPresent()) {
      response.put("success", false);
      response.put("message", "User not found");
      return response;
    }

    User foundUser = user.get();
    if (!passwordEncoder.matches(password, foundUser.getPassword())) {
      response.put("success", false);
      response.put("message", "Invalid password");
      return response;
    }

    String token = jwtUtil.generateToken(foundUser.getId(), foundUser.getEmail(), foundUser.getRole());

    response.put("success", true);
    response.put("message", "Login successful");
    response.put("token", token);
    response.put("userId", foundUser.getId());
    response.put("name", foundUser.getName());
    response.put("role", foundUser.getRole());
    return response;
  }
}
