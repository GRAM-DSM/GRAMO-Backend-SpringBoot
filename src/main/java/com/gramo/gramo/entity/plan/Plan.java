package com.gramo.gramo.entity.plan;

import com.gramo.gramo.entity.baseentity.BaseCalendar;
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
@Table(name = "plan_tbl")
public class Plan extends BaseCalendar {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

}
