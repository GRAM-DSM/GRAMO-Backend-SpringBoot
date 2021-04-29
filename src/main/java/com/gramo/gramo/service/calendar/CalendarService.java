package com.gramo.gramo.service.calendar;

import com.gramo.gramo.payload.response.CalendarListResponse;

import java.time.YearMonth;

public interface CalendarService {
    CalendarListResponse getCalendar(YearMonth date);
}
