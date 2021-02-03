package com.gramo.gramo.service.auth;

import com.gramo.gramo.payload.response.AccessTokenResponse;

public interface AuthService {

    AccessTokenResponse tokenRefresh(String token);

}
