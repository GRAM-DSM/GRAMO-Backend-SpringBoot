package com.gramo.gramo.entity.plan;

import com.gramo.gramo.entity.BasicCalendarRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends BasicCalendarRepository<Plan, Long> {

}
