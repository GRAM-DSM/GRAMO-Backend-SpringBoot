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
        return findUser(email);
    }

    public User getAuthUser() {
        return findUser(authenticationFacade.getUserEmail());
    }

    private User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

}
