package com.gramo.gramo.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String token;

}
