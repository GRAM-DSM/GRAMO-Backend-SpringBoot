package com.gramo.gramo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarContentResponse {

    private Integer id;

    private LocalDate date;

    private Long picuCount;

    private Long planCount;
}
