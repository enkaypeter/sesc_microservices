package dev.enkay.student_service.security;

import dev.enkay.student_service.config.AppProperties;
import dev.enkay.student_service.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

@Service
public class JwtService {
  private final AppProperties appProperties;
  private static final long EXPIRATION_HOURS = 24;

  public JwtService(AppProperties appProperties) {
    this.appProperties = appProperties;
  }

  public String generateToken(User user) {
    Instant expiry = Instant.now().plus(EXPIRATION_HOURS, ChronoUnit.HOURS);

    return Jwts.builder()
        .setSubject(user.getEmail())
        .claim("role", user.getRole())
        .setIssuedAt(new Date())
        .setExpiration(Date.from(expiry))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(appProperties.getJwt().getSecret().getBytes(StandardCharsets.UTF_8));
  }

  public Long getExpiry(String token) {
    return extractClaim(token, Claims::getExpiration).getTime() / 1000;
  }

  private <T> T extractClaim(String token, Function<Claims, T> resolver) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody();

    return resolver.apply(claims);
  }

  public String extractUsername(String token) {
    return parseToken(token).getBody().getSubject();
  }

  public boolean isTokenValid(String token, User user) {
    String username = extractUsername(token);
    return username.equals(user.getEmail()) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    Date expiration = parseToken(token).getBody().getExpiration();
    return expiration.before(new Date());
  }

  private Jws<Claims> parseToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token);
  }
}
