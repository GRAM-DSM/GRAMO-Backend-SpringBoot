package com.gramo.gramo.entity.user;

import com.gramo.gramo.entity.user.enums.Major;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tbl")
public class User {

    @Id
    private String email;

    private Boolean emailStatus;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private Major major;

}
