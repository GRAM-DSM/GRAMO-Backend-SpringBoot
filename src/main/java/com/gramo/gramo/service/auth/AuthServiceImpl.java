package com.gramo.gramo.service.auth;

import com.gramo.gramo.entity.refreshtoken.RefreshToken;
import com.gramo.gramo.entity.refreshtoken.RefreshTokenRepository;
import com.gramo.gramo.exception.InvalidTokenException;
import com.gramo.gramo.payload.response.AccessTokenResponse;
import com.gramo.gramo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${auth.jwt.exp.refresh}")
    private Long refreshTokenExp;

    @Override
    public AccessTokenResponse tokenRefresh(String token) {

        if(!jwtTokenProvider.isRefreshToken(token)) {   // refreshToken인지 확인한 후에 아니면 Exception 발생
            throw new InvalidTokenException();
        }

        RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)    // refreshToken 엔티티에서 찾고 없으면 Exception
                .orElseThrow(InvalidTokenException::new);

        refreshTokenRepository.save(refreshToken.update(refreshTokenExp));              // refreshToken 유효시간 초기화

        return new AccessTokenResponse(jwtTokenProvider.generateAccessToken(refreshToken.getEmail())); // refreshToken의 이메일을 통해 accessToken 재발급
    }

}
