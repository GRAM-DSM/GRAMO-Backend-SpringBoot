package com.gramo.gramo.entity.homework.embedded;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Term {

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Builder.Default                                // builder는 기본값이 null인데, 다른 기본 값 설정
    private LocalDate startDate = LocalDate.now();

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}
