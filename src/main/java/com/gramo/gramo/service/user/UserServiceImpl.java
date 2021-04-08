package com.gramo.gramo.service.user;

import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.payload.response.UserInfoListResponse;
import com.gramo.gramo.payload.response.UserInfoResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserInfoResponse getUserInfo() {
        return userRepository.findByEmail(authenticationFacade.getUserEmail())
                .map(user -> new UserInfoResponse(user.getEmail(), user.getName(), user.getMajor()))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserInfoListResponse getUserList() {
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        userRepository.findAll().forEach(
                user -> userInfoResponses.add(
                        new UserInfoResponse(user.getEmail(), user.getName(), user.getMajor())
                )
        );
        return new UserInfoListResponse(userInfoResponses);
    }
}
