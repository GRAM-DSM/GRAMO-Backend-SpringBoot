package com.gramo.gramo.entity.picu;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface PicuRepository extends CrudRepository<Picu, Long> {
    List<Picu> findAllBy();

    List<Picu> findAllByDate(LocalDate date);
}
