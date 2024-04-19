package com.nilknow.yifanerp2.config.security;

import com.nilknow.yifanerp2.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith(".css") || requestURI.endsWith(".js") || requestURI.endsWith(".png") || requestURI.endsWith(".jpg")) {
            // If it's a CSS, JS, PNG, or JPG file, let it pass through
            filterChain.doFilter(request, response);
            return;
        }
        if (isLoginRelatedRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = extractJwtToken(request);
        if (jwtToken == null) {
            log.info("redirect to login page when jwt in empty >>>>>>>>>>>>>>>>>>>>>");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } else {
            try {
                Claims claims = JwtUtil.getClaimsFromToken(jwtToken);
                TenantContextHolder.set(claims.get("companyId", Long.class));
                UserIdHolder.set(Long.valueOf(claims.getSubject()));
                filterChain.doFilter(request, response);
            } catch (SignatureException signatureException) {
                unsignForRequest(request, response);
            } catch (ExpiredJwtException expiredJwtException) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } catch (Exception e) {
                e.printStackTrace();
                filterChain.doFilter(request, response);
            }
        }
    }

    private void unsignForRequest(HttpServletRequest request, HttpServletResponse response) {
        MutableHttpServletRequest req = new MutableHttpServletRequest(request);
        Cookie cookie = new Cookie("Authorization", URLEncoder.encode("", StandardCharsets.UTF_8));
        cookie.setMaxAge(60 * 60); // 1 hour
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        try (PrintWriter writer = response.getWriter()) {
            writer.write("Unauthorized");
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isLoginRelatedRequest(HttpServletRequest request) {
        return ("GET".equals(request.getMethod()) || "POST".equals(request.getMethod()))
                && (request.getRequestURI().startsWith("/login")
                || request.getRequestURI().startsWith("/api/login")
                || request.getRequestURI().startsWith("/logout")
                || request.getRequestURI().startsWith("/api/logout")
        );
    }
    private String extractJwtToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) {
            return null;
        }
        Optional<Cookie> authorization = Arrays.stream(cookies).filter(x -> "Authorization".equals(x.getName())).findAny();
        if (authorization.isEmpty()) {
            return null;
        }
        String bearerToken = authorization.get().getValue();
        if (bearerToken != null && bearerToken.startsWith("Bearer+")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
