package com.gramo.gramo.service.user;

import com.gramo.gramo.payload.response.UserInfoListResponse;
import com.gramo.gramo.payload.response.UserInfoResponse;

public interface UserService {
    UserInfoResponse getUserInfo();

    UserInfoListResponse getUserList();
}
