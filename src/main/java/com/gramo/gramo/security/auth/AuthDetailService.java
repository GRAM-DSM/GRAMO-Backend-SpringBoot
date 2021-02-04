package com.gramo.gramo.security.auth;

import com.gramo.gramo.entity.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public AuthDetails loadUserByUsername(String username) throws UsernameNotFoundException {   // username(여기서는 이메일)을 통해 user를 찾아서 AuthDetails에 넘겨줌.
        return userRepository.findByEmail(username)
                .map(AuthDetails::new)
                .orElseThrow();
    }
}
