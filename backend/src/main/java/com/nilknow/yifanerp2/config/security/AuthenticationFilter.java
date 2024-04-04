package com.nilknow.yifanerp2.config.security;

import com.nilknow.yifanerp2.service.LoginUserService;
import com.nilknow.yifanerp2.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;

@Component
@Slf4j
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final String LOGIN_PAGE_URL = "/login";
    @Resource
    private LoginUserService loginUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if (requestURI.endsWith(".css") || requestURI.endsWith(".js") || requestURI.endsWith(".png") || requestURI.endsWith(".jpg")) {
            // If it's a CSS, JS, PNG, or JPG file, let it pass through
            filterChain.doFilter(request, response);
            return;
        }

        if (isLoginRelatedPageRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (isLoginApiRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }
        if (isLogoutRequest(request)) {
            unsignForRequest(request, response);
//            redirectToLoginPage(response);
            return;
        }
        if (isLoginRequest(request)) {
            login(request, response);
            return;
        }

        String jwtToken = extractJwtToken(request);
        if (jwtToken == null) {
            log.info("redirect to login page when jwt in empty >>>>>>>>>>>>>>>>>>>>>");
            redirectToLoginPage(response);
        } else {
            try {
                Claims claims = JwtUtil.getClaimsFromToken(jwtToken);
                TenantContextHolder.set(claims.get("companyId", Long.class));
                filterChain.doFilter(request, response);
            } catch (SignatureException signatureException) {
                // it means the signature is expired (because server restart)
                unsignForRequest(request, response);
            } catch (ExpiredJwtException expiredJwtException) {
                redirectToLoginPage(response);
            } catch (Exception e) {
                e.printStackTrace();
                filterChain.doFilter(request, response);
            }
        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String companyId = request.getHeader("Company-ID");
        if (!StringUtils.hasText(companyId)) {
            redirectToLoginFailBecauseOfCompanyPage(response);
            return;
        }
        boolean isAuthenticated = authenticate(username, password, companyId);
        if (isAuthenticated) {
            signForRequest(request, response, username, companyId);
            response.sendRedirect("/");
        } else {
            redirectToLoginFailPage(response);
        }
    }

    private void signForRequest(HttpServletRequest request, HttpServletResponse response, String username, String companyId) {
        MutableHttpServletRequest req = new MutableHttpServletRequest(request);
        String jwtToken = JwtUtil.generateToken(username, Long.valueOf(companyId));
        String wholeToken = "Bearer " + jwtToken;
        req.putHeader("Authorization", wholeToken);
        response.addCookie(new Cookie("Authorization", URLEncoder.encode(wholeToken, StandardCharsets.UTF_8)));
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    private void unsignForRequest(HttpServletRequest request, HttpServletResponse response) {
        MutableHttpServletRequest req = new MutableHttpServletRequest(request);
        Cookie cookie = new Cookie("Authorization", URLEncoder.encode("", StandardCharsets.UTF_8));
        cookie.setMaxAge(60 * 60); // 1 hour
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && "/login".equals(request.getRequestURI());
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) &&
                ("/logout".equals(request.getRequestURI()) || "/api/logout".equals(request.getRequestURI()));
    }

    private boolean isLoginRelatedPageRequest(HttpServletRequest request) {
        return "GET".equals(request.getMethod()) && request.getRequestURI().startsWith("/login");
    }

    private boolean isLoginApiRequest(HttpServletRequest request) {
        return "POST".equals(request.getMethod()) && request.getRequestURI().startsWith("/api/login");
    }

    private boolean authenticate(String username, String password, String companyId) {
        return loginUserService.authenticate(username, password, Long.valueOf(companyId));
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

    private void redirectToLoginPage(HttpServletResponse response) throws IOException {
        response.sendRedirect(LOGIN_PAGE_URL);
    }

    private void redirectToLoginFailPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login/fail");
    }

    private void redirectToLoginFailBecauseOfCompanyPage(HttpServletResponse response) throws IOException {
        response.sendRedirect("/login/fail-because-of-company");
    }
}
