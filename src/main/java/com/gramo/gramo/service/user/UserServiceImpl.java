package com.gramo.gramo.service.user;

import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.payload.response.UserInfoResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserInfoResponse getUserInfo() {
        return userRepository.findByEmail(authenticationFacade.getUserEmail())
                .map(user -> new UserInfoResponse(user.getEmail(), user.getMajor()))
                .orElseThrow(UserNotFoundException::new);
    }
}
