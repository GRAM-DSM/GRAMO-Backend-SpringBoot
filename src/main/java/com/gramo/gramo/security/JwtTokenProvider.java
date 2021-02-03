package com.gramo.gramo.security;

import com.gramo.gramo.exception.InvalidTokenException;
import com.gramo.gramo.security.auth.AuthDetailService;
import com.gramo.gramo.security.auth.AuthDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Value("${auth.jwt.exp.access}")
    private Long accessTokenExp;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshTokenExp;

    @Value("${auth.jwt.header}")
    private String header;

    @Value("${auth.jwt.prefix}")
    private String prefix;

    private final AuthDetailService authDetailService;

    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExp * 1000))
                .claim("type", "access_token")
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setSubject(email)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExp * 1000))
                .claim("type", "refresh_token")
                .compact();
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(header);
        if(bearerToken != null && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
            return true;
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public String getEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public Authentication getAuthentication(String token) {
        AuthDetails authDetails = authDetailService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(authDetails, "", authDetails.getAuthorities());
    }

    public boolean isRefreshToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("type").equals("refresh_token");
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public boolean getEmailStatus(String token) {
        AuthDetails authDetails = authDetailService.loadUserByUsername(getEmail(token));
        return authDetails.getEmailStatus();
    }

}
