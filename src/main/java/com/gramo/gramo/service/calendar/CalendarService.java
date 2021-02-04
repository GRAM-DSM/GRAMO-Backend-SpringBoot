package com.gramo.gramo.service.calendar;

import com.gramo.gramo.payload.response.CalendarContentResponse;

import java.time.LocalDate;
import java.util.List;

public interface CalendarService {
    List<CalendarContentResponse> getCalendar(LocalDate date);
}
