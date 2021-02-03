package com.gramo.gramo.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AccessTokenResponse {

    @JsonProperty("access_token")
    private String accessToken;

}
