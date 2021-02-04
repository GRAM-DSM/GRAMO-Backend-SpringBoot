package com.gramo.gramo.service.calendar;

import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.payload.response.CalendarContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final PicuRepository picuRepository;
    private final PlanRepository planRepository;

    @Override
    public List<CalendarContentResponse> getCalendar(LocalDate date) {
        List<CalendarContentResponse> calendarContentResponses = new ArrayList<>();

        date.getDayOfMonth();
        LocalDate localDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        while (LocalDate.of(date.getYear(),date.getMonth(),date.getDayOfMonth()).isAfter(localDate)) {
            calendarContentResponses.add(
                    CalendarContentResponse.builder()
                            .date(localDate)
                            .picuCount(picuRepository.countAllByDate(localDate))
                            .planCount(planRepository.countAllByDate(localDate))
                            .build()
            );
            localDate = localDate.plusDays(1);
        }

        return calendarContentResponses;
    }
}
