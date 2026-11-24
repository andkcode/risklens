package com.risklens.core.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class LoggingContextFilter extends OncePerRequestFilter {

  public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
  public static final String CORRELATION_ID_MDC_KEY = "correlationId";
  public static final String USER_ID_MDC_KEY = "userId";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    String correlationId = request.getHeader(CORRELATION_ID_HEADER);
    if (correlationId == null || correlationId.isBlank()) {
      correlationId = UUID.randomUUID().toString();
    }

    MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
    response.setHeader(CORRELATION_ID_HEADER, correlationId);

    String userId = resolveUserId();
    if (userId != null && !userId.isBlank()) {
      MDC.put(USER_ID_MDC_KEY, userId);
    }

    try {
      filterChain.doFilter(request, response);
    } finally {
      String postAuthUserId = resolveUserId();
      if (postAuthUserId != null && !postAuthUserId.isBlank() && MDC.get(USER_ID_MDC_KEY) == null) {
        MDC.put(USER_ID_MDC_KEY, postAuthUserId);
      }
      MDC.remove(CORRELATION_ID_MDC_KEY);
      MDC.remove(USER_ID_MDC_KEY);
    }
  }

  private String resolveUserId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
      return null;
    }

    Object principal = auth.getPrincipal();
    if (principal instanceof UserDetails userDetails) {
      return userDetails.getUsername();
    }
    if (principal instanceof String str) {
      return str;
    }

    return auth.getName();
  }
}
