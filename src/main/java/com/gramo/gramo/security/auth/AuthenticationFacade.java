package com.gramo.gramo.security.auth;

import com.gramo.gramo.security.JwtTokenProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacade {

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getUserEmail() {
        return this.getAuthentication().getName();
    }

    public boolean isLogin() {
        return this.getAuthentication() != null;
    }
}
