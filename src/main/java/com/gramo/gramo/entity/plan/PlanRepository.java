package com.gramo.gramo.entity.plan;

import com.gramo.gramo.entity.BasicCalendarRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends BasicCalendarRepository<Plan, Long> {

}
