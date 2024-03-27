package com.nilknow.yifanerp2.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 100;

    public static String generateToken(String userId, Long companyId) {
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .subject(userId)
                .claims(Map.of("companyId", companyId))
                .expiration(expireDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    public static Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static boolean validateToken(String authToken) {
        try {
            Jwts.parser().verifyWith(SECRET_KEY).build().parseSignedClaims(authToken);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
