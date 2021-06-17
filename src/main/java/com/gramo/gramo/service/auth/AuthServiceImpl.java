package com.gramo.gramo.service.auth;

import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.InvalidTokenException;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.payload.request.AuthRequest;
import com.gramo.gramo.payload.response.TokenRefreshResponse;
import com.gramo.gramo.payload.response.TokenResponse;
import com.gramo.gramo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse signIn(AuthRequest authRequest) {
        return userRepository.findByEmail(authRequest.getEmail())
                .filter(user -> passwordEncoder.matches(authRequest.getPassword(), user.getPassword()))
                .map(user -> TokenResponse.builder()
                        .accessToken(jwtTokenProvider.generateAccessToken(user.getEmail()))
                        .major(user.getMajor())
                        .name(user.getName())
                        .refreshToken(jwtTokenProvider.generateRefreshToken(user.getEmail()))
                        .build())
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public TokenRefreshResponse tokenRefresh(String refreshToken) {
        if(!jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidTokenException();
        }

        String email = jwtTokenProvider.getEmail(refreshToken);
        return new TokenRefreshResponse(jwtTokenProvider.generateAccessToken(email));
    }

}
