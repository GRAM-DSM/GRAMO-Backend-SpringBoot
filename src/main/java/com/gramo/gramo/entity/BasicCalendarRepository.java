package com.gramo.gramo.entity;

import com.gramo.gramo.entity.plan.Plan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@NoRepositoryBean
public interface BasicCalendarRepository<T, ID extends Serializable> extends CrudRepository<T, ID> {
    List<T> findAllBy();

    List<T> findAllByDateOrderByIdDesc(LocalDate date);

    List<T> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
