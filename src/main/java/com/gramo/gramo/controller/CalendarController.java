package com.gramo.gramo.controller;

import com.gramo.gramo.payload.response.CalendarContentResponse;
import com.gramo.gramo.service.calendar.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    @GetMapping
    public List<CalendarContentResponse> getCalendar(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localDate) {
        return calendarService.getCalendar(localDate);
    }
}
