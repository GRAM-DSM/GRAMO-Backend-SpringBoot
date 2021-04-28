package com.gramo.gramo.service.calendar;

import com.gramo.gramo.entity.picu.PicuRepository;
import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.payload.response.CalendarContentResponse;
import com.gramo.gramo.payload.response.CalendarListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final PicuRepository picuRepository;
    private final PlanRepository planRepository;

    @Override
    public CalendarListResponse getCalendar(YearMonth date) {
        HashMap<LocalDate, Integer> picuMap = new HashMap<>();  // hash 알고리즘을 통해 그냥 map보다 빠름
        HashMap<LocalDate, Integer> planMap = new HashMap<>();
        List<LocalDate> picuDates = new ArrayList<>();
        List<LocalDate> planDates = new ArrayList<>();

        HashMap<String, LocalDate> dateMap = getDate(date);

        picuRepository.findAllByDateBetween(dateMap.get("startDate"), dateMap.get("endDate"))   // get the picu list
                .forEach(picu -> picuDates.add(picu.getDate()));                                // extract picu's date
        planRepository.findAllByDateBetween(dateMap.get("startDate"), dateMap.get("endDate"))   // get the plan list
                .forEach(plan -> planDates.add(plan.getDate()));                                // extract plan's date

        getCalendarMap(picuDates, picuMap);// get map with date
        getCalendarMap(planDates, planMap);

        return new CalendarListResponse(buildResponse(picuMap, planMap, dateMap));
    }

    private List<CalendarContentResponse> buildResponse(HashMap<LocalDate, Integer> picuMap,
                                                        HashMap<LocalDate, Integer> planMap,
                                                        HashMap<String, LocalDate> dateMap) {

        LocalDate current = dateMap.get("startDate");

        List<CalendarContentResponse> calendarContentResponses = new ArrayList<>();

        while(!current.isAfter(dateMap.get("endDate"))) {
            calendarContentResponses.add(
                    CalendarContentResponse.builder()
                            .picuCount(picuMap.get(current) != null ? picuMap.get(current).longValue(): 0)
                            .planCount(planMap.get(current) != null ? planMap.get(current).longValue(): 0)
                            .id(current.getDayOfMonth())
                            .date(current)
                            .build()
            );
            current = current.plusDays(1L);
        }

        return calendarContentResponses;

    }

    private void getCalendarMap(List<LocalDate> dates,
                                HashMap<LocalDate, Integer> map) {      // 굳이 반환하지 않고 void로 해도, 여기서의 hashMap 수정사항이 적용 됨
        for(LocalDate date : dates) {
            map.merge(date, 1, Integer::sum);   // map에서 get을 하고, 있으면 1을 더해서 넣고 없으면 1을 넣음
        }
    }

    private HashMap<String, LocalDate> getDate(YearMonth date) {
        HashMap<String, LocalDate> dateMap = new HashMap<>();

        LocalDate startDate = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate endDate = LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth());

        dateMap.put("startDate", startDate);
        dateMap.put("endDate", endDate);

        return dateMap;
    }
}
