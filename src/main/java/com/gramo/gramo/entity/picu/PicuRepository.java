package com.gramo.gramo.entity.picu;

import com.gramo.gramo.entity.BasicCalendarRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PicuRepository extends BasicCalendarRepository<Picu, Long> {
}
