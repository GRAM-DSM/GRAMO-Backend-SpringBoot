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
                .setIssuedAt(new Date())                                        // 생성일
                .setSubject(email)                                              // 암호화할 것
                .signWith(SignatureAlgorithm.HS256, secretKey)                  // 암호화 방식, 시크릿 키
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExp * 1000)) // 파기일
                .claim("type", "refresh_token")                     // 나중에 이 토큰이 refresh_token인지 여부 확인을 위한 이름
                .compact();                                                    // 적용
    }

    public String resolveToken(HttpServletRequest request) {
        System.out.println("resolving token");
        String bearerToken = request.getHeader(header);             // request에서 header 부분을 추출
        if(bearerToken != null && bearerToken.startsWith(prefix)) {
            System.out.println("token resolve success");
            return bearerToken.substring(7);                        // Bearer 으로 시작하기 때문에 Bearer의 뒷부분만 반환
        }
        System.out.println("resolve error");
        return null;
    }

    public boolean validateToken(String token) {
        try {
            System.out.println("validate token");
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();        // 토큰을 추출했을 때, 오류가 발생하지 않으면 true 반환
            System.out.println("validate token success");
            return true;
        } catch (Exception e) {
            System.out.println("validate token error");
            throw new InvalidTokenException();
        }
    }

    public String getEmail(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject(); // 토큰에서 email만 추출해서 가져옴
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public Authentication getAuthentication(String token) {
        AuthDetails authDetails = authDetailService.loadUserByUsername(getEmail(token));                // 토큰에서 email 추출하고 그걸로 AuthDetails에 넣어줌
        return new UsernamePasswordAuthenticationToken(authDetails, "", authDetails.getAuthorities());  // 인증용 객체 생성
    }

    public boolean isRefreshToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().get("type").equals("refresh_token");      // 토큰 생성할 때 같이 저장한 토큰 종류를 가져와서 refresh_token인지 판단
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public boolean getEmailStatus(String token) {
        AuthDetails authDetails = authDetailService.loadUserByUsername(getEmail(token));
        return authDetails.getEmailStatus();
    }

}
