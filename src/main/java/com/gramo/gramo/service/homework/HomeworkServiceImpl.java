package com.gramo.gramo.service.homework;

import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.homework.HomeworkRepository;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.exception.HomeworkNotFoundException;
import com.gramo.gramo.exception.LoginException;
import com.gramo.gramo.exception.PermissionMismatchException;
import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.HomeworkResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final AuthenticationFacade authenticationFacade;
    private final HomeworkRepository homeworkRepository;

    @Override
    public void saveHomework(HomeworkRequest homeworkRequest) {

        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        homeworkRepository.save(
                Homework.builder()
                        .teacherEmail(authenticationFacade.getUserEmail())
                        .description(homeworkRequest.getDescription())
                        .endDate(homeworkRequest.getEndDate())
                        .isRejected(false)
                        .major(homeworkRequest.getMajor())
                        .isSubmitted(false)
                        .startDate(LocalDate.now())
                        .studentEmail(homeworkRequest.getStudentEmail())
                        .title(homeworkRequest.getTitle())
                        .build()
        );

    }

    @Override
    public void deleteHomework(Long homeworkId) {
        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if(!authenticationFacade.getUserEmail().equals(homework.getTeacherEmail())) {
            throw new PermissionMismatchException();
        }

        homeworkRepository.delete(homework);
    }

    @Override
    public HomeworkResponse getHomework(Long homeworkId) {
        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        return HomeworkResponse.builder()
                .major(homework.getMajor())
                .teacherName(homework.getTeacherEmail())
                .studentName(homework.getStudentEmail())
                .isRejected(homework.getIsRejected())
                .startDate(homework.getStartDate())
                .endDate(homework.getEndDate())
                .description(homework.getDescription())
                .title(homework.getTitle())
                .build();
    }

    @Override
    public List<HomeworkResponse> getAssignedHomework() {

        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        List<Homework> homework = homeworkRepository.findAllByIsSubmittedFalseAndStudentEmailOrderByStartDateDesc(authenticationFacade.getUserEmail());
        List<HomeworkResponse> homeworkResponses = new ArrayList<>();

        for(Homework work : homework) {
            homeworkResponses.add(
                    HomeworkResponse.builder()
                            .homeworkId(work.getId())
                            .description(work.getDescription())
                            .endDate(work.getEndDate())
                            .startDate(work.getStartDate())
                            .isRejected(work.getIsRejected())
                            .major(work.getMajor())
                            .studentName(work.getStudentEmail())
                            .teacherName(work.getTeacherEmail())
                            .title(work.getTitle())
                            .build()
            );
        }

        return homeworkResponses;
    }

    @Override
    public List<HomeworkResponse> getSubmittedHomework() {
        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        List<Homework> homework = homeworkRepository.findAllByIsSubmittedTrueAndStudentEmailOrderByStartDateDesc(authenticationFacade.getUserEmail());
        List<HomeworkResponse> homeworkResponses = new ArrayList<>();

        for(Homework work : homework) {
            homeworkResponses.add(
                    HomeworkResponse.builder()
                            .homeworkId(work.getId())
                            .description(work.getDescription())
                            .endDate(work.getEndDate())
                            .startDate(work.getStartDate())
                            .isRejected(work.getIsRejected())
                            .major(work.getMajor())
                            .studentName(work.getStudentEmail())
                            .teacherName(work.getTeacherEmail())
                            .title(work.getTitle())
                            .build()
            );
        }

        return homeworkResponses;
    }

    @Override
    public List<HomeworkResponse> getOrderedHomework() {

        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        List<Homework> homework = homeworkRepository.findAllByTeacherEmailOrderByStartDateDesc(authenticationFacade.getUserEmail());
        List<HomeworkResponse> homeworkResponses = new ArrayList<>();

        for(Homework work : homework) {
            homeworkResponses.add(
                    HomeworkResponse.builder()
                            .homeworkId(work.getId())
                            .description(work.getDescription())
                            .endDate(work.getEndDate())
                            .startDate(work.getStartDate())
                            .isRejected(work.getIsRejected())
                            .major(work.getMajor())
                            .studentName(work.getStudentEmail())
                            .teacherName(work.getTeacherEmail())
                            .title(work.getTitle())
                            .build()
            );
        }

        return homeworkResponses;
    }

    @Override
    public void submitHomework(Long homeworkId) {

        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if(!homework.getStudentEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        homeworkRepository.save(homework.submitHomework());

    }

    @Override
    public void rejectHomework(Long homeworkId) {

        if(!authenticationFacade.isLogin()) {
            throw new LoginException();
        }

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);
        
        if(!homework.getTeacherEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        homeworkRepository.save(homework.rejectHomework());
    }
}
