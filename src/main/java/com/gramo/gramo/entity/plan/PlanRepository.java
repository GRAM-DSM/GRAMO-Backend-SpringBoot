package com.gramo.gramo.entity.plan;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends CrudRepository<Plan, Long> {
    List<Plan> findAllBy();

    List<Plan> findAllByDateOrderByIdDesc(LocalDate date);

    Long countAllByDate(LocalDate date);
}
