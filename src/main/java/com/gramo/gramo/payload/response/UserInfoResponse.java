package com.gramo.gramo.payload.response;

import com.gramo.gramo.entity.user.enums.Major;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private String name;

    private Major major;
}
