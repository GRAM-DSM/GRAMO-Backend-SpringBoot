package com.gramo.gramo.entity.picu;

import com.gramo.gramo.entity.BasicCalendarRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PicuRepository extends BasicCalendarRepository<Picu, Long> {
}
