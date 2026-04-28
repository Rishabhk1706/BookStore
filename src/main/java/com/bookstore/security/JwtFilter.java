package com.bookstore.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {
      String token = authHeader.substring(7);

      if (jwtUtil.validateToken(token)) {
        String userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("email", jwtUtil.getEmailFromToken(token));
        request.setAttribute("role", jwtUtil.getRoleFromToken(token));
      }
    }

    filterChain.doFilter(request, response);
  }
}
