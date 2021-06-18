package com.gramo.gramo.entity.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface NoticeRepository extends CrudRepository<Notice, Long> {

    Page<Notice> findAllByOrderByCreatedAtDesc(Pageable pageable);

}
