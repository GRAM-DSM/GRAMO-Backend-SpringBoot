package com.gramo.gramo.security;

import com.gramo.gramo.exception.InvalidTokenException;
import com.gramo.gramo.security.auth.AuthDetailService;
import com.gramo.gramo.security.auth.AuthDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Value("${auth.jwt.header}")
    private String header;

    @Value("${auth.jwt.prefix}")
    private String prefix;

    private final AuthDetailService authDetailService;

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(header);             // request에서 header 부분을 추출
        if(bearerToken != null && bearerToken.startsWith(prefix)) {
            return bearerToken.substring(7);                        // Bearer 으로 시작하기 때문에 Bearer의 뒷부분만 반환
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            return getTokenBody(token)
                    .getExpiration().after(new Date());        // 토큰을 추출했을 때, 오류가 발생하지 않고 해당 토큰의 유효일이 괜찮으면 true
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public String getEmail(String token) {
        try {
            return getTokenBody(token).getSubject(); // 토큰에서 email만 추출해서 가져옴
        } catch (Exception e) {
            throw new InvalidTokenException();
        }
    }

    public Authentication getAuthentication(String token) {
        AuthDetails authDetails = authDetailService.loadUserByUsername(getEmail(token));                // 토큰에서 email 추출하고 그걸로 AuthDetails에 넣어줌
        return new UsernamePasswordAuthenticationToken(authDetails, "", authDetails.getAuthorities());  // 인증용 객체 생성
    }

    public boolean getEmailStatus(String token) {
        AuthDetails authDetails = authDetailService.loadUserByUsername(getEmail(token));
        return authDetails.getEmailStatus();
    }

    private Claims getTokenBody(String token) {
        return Jwts.parser().setSigningKey(Base64.getEncoder()
                .encodeToString(secretKey.getBytes())).parseClaimsJws(token).getBody();
    }

}
