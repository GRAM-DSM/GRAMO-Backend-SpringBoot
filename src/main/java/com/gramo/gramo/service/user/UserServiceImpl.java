package com.gramo.gramo.service.user;

import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.entity.verifynumber.VerifyNumberRepository;
import com.gramo.gramo.entity.verifyuser.VerifyUser;
import com.gramo.gramo.entity.verifyuser.VerifyUserRepository;
import com.gramo.gramo.exception.UserAlreadyExistException;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.exception.VerifyNumNotFoundException;
import com.gramo.gramo.payload.request.SignUpRequest;
import com.gramo.gramo.payload.request.VerifyRequest;
import com.gramo.gramo.payload.response.UserInfoListResponse;
import com.gramo.gramo.payload.response.UserInfoResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationFacade authenticationFacade;
    private final PasswordEncoder passwordEncoder;
    private final VerifyNumberRepository verifyNumberRepository;
    private final VerifyUserRepository verifyUserRepository;

    @Override
    public UserInfoResponse getUserInfo() {
        return userRepository.findByEmail(authenticationFacade.getUserEmail())
                .map(user -> new UserInfoResponse(user.getEmail(), user.getName(), user.getMajor()))
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public UserInfoListResponse getUserList() {
        List<UserInfoResponse> userInfoResponses = new ArrayList<>();
        userRepository.findAllBy()
                .stream().filter(user -> !user.getEmail().equals(authenticationFacade.getUserEmail()))
                .forEach(user -> userInfoResponses.add(
                        new UserInfoResponse(user.getEmail(), user.getName(), user.getMajor()))
                );
        return new UserInfoListResponse(userInfoResponses);
    }

    @Override
    public void signUp(SignUpRequest request) {
        if (userRepository.existsById(request.getEmail())) {
            throw new UserAlreadyExistException();
        }

        verifyUserRepository.findByEmail(request.getEmail())
                .ifPresentOrElse(
                        verifyUser -> userRepository.save(
                                User.builder()
                                        .email(request.getEmail())
                                        .emailStatus(true)
                                        .major(request.getMajor())
                                        .name(request.getName())
                                        .password(passwordEncoder.encode(request.getPassword()))
                                        .build()
                        ),
                        () -> {
                            throw new VerifyNumNotFoundException();
                        }
                );
    }

    @Override
    public void verify(VerifyRequest request) {
        verifyNumberRepository.findByEmail(request.getEmail())
                .filter(verifyNumber -> verifyNumber.verifyNumber(request.getCode()))
                .map(verifyNumber -> verifyUserRepository.save(new VerifyUser(request.getEmail())))
                .orElseThrow(VerifyNumNotFoundException::new);
    }

}
