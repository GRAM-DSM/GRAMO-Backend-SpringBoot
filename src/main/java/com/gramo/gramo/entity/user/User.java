package com.gramo.gramo.entity.user;

import com.gramo.gramo.entity.notice.Notice;
import com.gramo.gramo.entity.user.enums.Major;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tbl")
public class User {

    @Id
    private String email;

    @Column(nullable = false)
    private Boolean emailStatus;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Major major;

    private String token;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notice> notices;

    public void updateToken(String token) {
        this.token = token;
    }
}
