package com.gramo.gramo.factory;

import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public User getAuthUser() {
        return userRepository.findByEmail(authenticationFacade.getUserEmail())
                .orElseThrow(UserNotFoundException::new);
    }

}
