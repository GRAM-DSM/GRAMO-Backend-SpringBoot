package com.gramo.gramo.entity.homework;

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
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String studentEmail;

    @Enumerated(EnumType.STRING)
    private Major major;

    private String teacherEmail;

    private Boolean isSubmitted;

    private Boolean isRejected;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    public Homework submitHomework() {
        this.isSubmitted = true;
        return this;
    }

    public Homework rejectHomework() {
        this.isRejected = true;
        this.isSubmitted = false;
        return this;
    }

}
