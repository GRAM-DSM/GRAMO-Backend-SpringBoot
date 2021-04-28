package com.gramo.gramo.service.calendar;

import com.gramo.gramo.payload.response.CalendarContentResponse;
import com.gramo.gramo.payload.response.CalendarListResponse;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public interface CalendarService {
    CalendarListResponse getCalendar(YearMonth date);
}
