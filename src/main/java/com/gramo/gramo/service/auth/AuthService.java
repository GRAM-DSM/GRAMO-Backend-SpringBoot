package com.gramo.gramo.service.auth;

import com.gramo.gramo.payload.request.AuthRequest;
import com.gramo.gramo.payload.response.TokenRefreshResponse;
import com.gramo.gramo.payload.response.TokenResponse;

public interface AuthService {
    TokenResponse signIn(AuthRequest authRequest);

    TokenRefreshResponse tokenRefresh(String refreshToken);
}
