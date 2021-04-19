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

        Map<String, LocalDate> dateMap = getDate(date);

        List<Picu> picus = picuRepository.findAllByDateBetween(dateMap.get("startDate"), dateMap.get("endDate"));
        List<Plan> plans = planRepository.findAllByDateBetween(dateMap.get("startDate"), dateMap.get("endDate"));

        picuMap = getPicuMap(picus, picuMap);
        planMap = getPlanMap(plans, planMap);

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

    private HashMap<LocalDate, Integer> getPicuMap(List<Picu> picus,
                                                           HashMap<LocalDate, Integer> picuMap) {

        for(Picu picu : picus) {
            if(picuMap.get(picu.getDate()) == null) {
                picuMap.put(picu.getDate(), 1);
            } else {
                picuMap.put(picu.getDate(), picuMap.get(picu.getDate()) + 1);
            }
        }
        return picuMap;
    }

    private HashMap<LocalDate, Integer> getPlanMap(List<Plan> plans,
                                                   HashMap<LocalDate, Integer> planMap) {
        for (Plan plan : plans) {
            if (planMap.get(plan.getDate()) == null) {
                planMap.put(plan.getDate(), 1);
            } else {
                planMap.put(plan.getDate(), planMap.get(plan.getDate()) + 1);
            }
        }
        return planMap;
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
