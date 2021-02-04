package com.gramo.gramo.entity.homework;

import com.gramo.gramo.entity.user.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    List<Homework> findAllByIsSubmittedFalseAndStudentEmailOrderByStartDateDesc(String studentEmail);

    List<Homework> findAllByIsSubmittedTrueAndStudentEmailOrderByStartDateDesc(String studentEmail);

    List<Homework> findAllByTeacherEmailOrderByStartDateDesc(String teacherEmail);

}
