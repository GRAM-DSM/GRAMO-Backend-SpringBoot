package com.gramo.gramo.entity.refreshtoken;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;

    private Long refreshExp;

    public RefreshToken update(Long refreshExp) {
        this.refreshExp = refreshExp;
        return this;
    }
}
