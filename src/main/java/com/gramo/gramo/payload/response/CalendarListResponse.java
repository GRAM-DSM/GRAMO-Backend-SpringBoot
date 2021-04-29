package com.gramo.gramo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CalendarListResponse {
    private List<CalendarContentResponse> calendarContentResponses;
}
