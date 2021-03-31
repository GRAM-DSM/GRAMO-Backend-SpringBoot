package com.gramo.gramo.entity.homework;

import com.gramo.gramo.entity.user.enums.Major;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {

    List<Homework> findAllByIsSubmittedFalseAndStudentEmailOrderByIdDesc(String studentEmail);

    List<Homework> findAllByIsSubmittedTrueAndStudentEmailOrderByIdDesc(String studentEmail);

    List<Homework> findAllByTeacherEmailOrderByIdDesc(String teacherEmail);

}
