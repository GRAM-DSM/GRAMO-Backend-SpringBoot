package com.gramo.gramo.service.user;

import com.gramo.gramo.payload.request.SignUpRequest;
import com.gramo.gramo.payload.request.VerifyRequest;
import com.gramo.gramo.payload.response.UserInfoListResponse;
import com.gramo.gramo.payload.response.UserInfoResponse;

public interface UserService {
    UserInfoResponse getUserInfo();

    UserInfoListResponse getUserList();

    void signUp(SignUpRequest request);

    void verify(VerifyRequest request);
}
