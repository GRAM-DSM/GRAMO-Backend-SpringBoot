package com.gramo.gramo.controller;

import com.gramo.gramo.payload.response.CalendarContentResponse;
import com.gramo.gramo.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    @GetMapping("/{date}")
    public List<CalendarContentResponse> getCalendar(@PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        return calendarService.getCalendar(date);
    }
}
