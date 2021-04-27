package com.gramo.gramo.factory;

import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

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
        System.out.println("전체 유저");
        userRepository.findAllBy()
                .forEach(user -> System.out.println(user.getEmail()));
        System.out.println("여까지");

        System.out.println(userRepository.existsById(email));
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}
