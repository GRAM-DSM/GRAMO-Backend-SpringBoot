package com.gramo.gramo.entity.baseentity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseCalendar {  // @MappedSuperclass는 보통 abstract class를 추천한다. 보통 인스턴스가 구현되지 않기 때문이다.
    @Id
    @GeneratedValue
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate date;
}
