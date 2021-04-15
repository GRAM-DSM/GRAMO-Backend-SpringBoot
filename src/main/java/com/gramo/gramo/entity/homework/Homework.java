package com.gramo.gramo.entity.homework;

import com.gramo.gramo.entity.baseentity.BaseId;
import com.gramo.gramo.entity.homework.embedded.Status;
import com.gramo.gramo.entity.homework.embedded.Term;
import com.gramo.gramo.entity.user.enums.Major;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "homework_tbl")
public class Homework extends BaseId {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String studentEmail;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Major major;

    @Column(nullable = false)
    private String teacherEmail;

    @Embedded
    private Status status;

    @Embedded
    private Term term;

}
