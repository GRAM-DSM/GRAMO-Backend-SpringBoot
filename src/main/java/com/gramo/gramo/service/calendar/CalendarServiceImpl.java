package com.gramo.gramo.service.calendar;

import com.gramo.gramo.entity.BasicCalendarRepository;
import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.entity.plan.Plan;
import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.payload.response.CalendarContentResponse;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final PicuRepository picuRepository;
    private final PlanRepository planRepository;

    @Override
    public List<CalendarContentResponse> getCalendar(LocalDate date) {
        HashMap<LocalDate, Integer> picuMap = new HashMap<>();
        HashMap<LocalDate, Integer> planMap = new HashMap<>();
        List<LocalDate> picuDates = new ArrayList<>();
        List<LocalDate> planDates = new ArrayList<>();

        Map<String, LocalDate> dateMap = getDate(date);

        picuRepository.findAllByDateBetween(dateMap.get("startDate"), dateMap.get("endDate"))
                .forEach(picu -> picuDates.add(picu.getDate()));
        planRepository.findAllByDateBetween(dateMap.get("startDate"), dateMap.get("endDate"))
                .forEach(plan -> planDates.add(plan.getDate()));

        picuMap = getCalendarMap(picuDates, picuMap);
        planMap = getCalendarMap(planDates, planMap);

        return buildResponse(picuMap, planMap, dateMap);
    }

    private List<CalendarContentResponse> buildResponse(HashMap<LocalDate, Integer> picuMap,
                                                        HashMap<LocalDate, Integer> planMap,
                                                        Map<String, LocalDate> dateMap) {

        LocalDate current = dateMap.get("startDate");

        List<CalendarContentResponse> calendarContentResponses = new ArrayList<>();

        while(!current.isAfter(dateMap.get("endDate"))) {
            calendarContentResponses.add(
                    CalendarContentResponse.builder()
                            .picuCount(picuMap.get(current) != null ? picuMap.get(current).longValue(): 0)
                            .planCount(planMap.get(current) != null ? planMap.get(current).longValue(): 0)
                            .date(current)
                            .build()
            );
            current = current.plusDays(1L);
        }

        return calendarContentResponses;

    }

    private HashMap<LocalDate, Integer> getCalendarMap(List<LocalDate> dates,
                                                           HashMap<LocalDate, Integer> map) {

        for(LocalDate date : dates) {
            if(map.get(date) == null) {
                map.put(date, 1);
            } else {
                map.put(date, map.get(date) + 1);
            }
        }
        return map;
    }

    private Map<String, LocalDate> getDate(LocalDate date) {
        Map<String, LocalDate> dateMap = new HashMap<>();

        LocalDate startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate endDate = LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth());

        dateMap.put("startDate", startDate);
        dateMap.put("endDate", endDate);

        return dateMap;
    }
}
