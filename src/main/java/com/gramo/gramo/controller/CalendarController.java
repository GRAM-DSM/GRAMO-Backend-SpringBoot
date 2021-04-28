package com.gramo.gramo.controller;

import com.gramo.gramo.payload.response.CalendarContentResponse;
import com.gramo.gramo.payload.response.CalendarListResponse;
import com.gramo.gramo.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    @GetMapping("/{date}")
    public CalendarListResponse getCalendar(@PathVariable @DateTimeFormat(pattern = "yyyy-MM") YearMonth date) {
        return calendarService.getCalendar(date);
    }
}
