package com.gramo.gramo.entity.picu;

import com.gramo.gramo.entity.baseentity.BaseCalendar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "picu_tbl")
public class Picu extends BaseCalendar {

    @Column(nullable = false)
    private String userEmail;

    @Column(nullable = false)
    private String description;

}
