package com.gramo.gramo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class TokenFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);     // request에서 토큰 값을 추출

        if (token != null && jwtTokenProvider.validateToken(token)
                && jwtTokenProvider.getEmailStatus(token)) {      // 만약 토큰이 null이 아니고, 유효한 토큰이고, 이메일 검증을 받은 이메일이면 실행
            Authentication authentication = jwtTokenProvider.getAuthentication(token);                              // 토큰 값을 가져옴
            SecurityContextHolder.getContext().setAuthentication(authentication);                                   // 값을 SecurityContextHolder에 넣음
        }

        chain.doFilter(request, response);

    }

}
