package com.gramo.gramo.service.homework;

import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.homework.HomeworkRepository;
import com.gramo.gramo.entity.homework.embedded.Term;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final AuthenticationFacade authenticationFacade;
    private final HomeworkRepository homeworkRepository;

    @Override
    public void saveHomework(HomeworkRequest homeworkRequest) {

        Term term = Term.builder()
                .endDate(homeworkRequest.getEndDate())
                .startDate(LocalDate.now())
                .build();

        homeworkRepository.save(
                Homework.builder()
                        .teacherEmail(authenticationFacade.getUserEmail())
                        .description(homeworkRequest.getDescription())
                        .isRejected(false)
                        .major(homeworkRequest.getMajor())
                        .isSubmitted(false)
                        .studentEmail(homeworkRequest.getStudentEmail())
                        .title(homeworkRequest.getTitle())
                        .term(term)
                        .build()
        );

    }

    @Override
    public void deleteHomework(Long homeworkId) {

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if(!authenticationFacade.getUserEmail().equals(homework.getTeacherEmail())) {
            throw new PermissionMismatchException();
        }

        homeworkRepository.delete(homework);
    }

    @Override
    public HomeworkResponse getHomework(Long homeworkId) {

        return buildResponse(
                homeworkRepository.findById(homeworkId)
                        .orElseThrow(HomeworkNotFoundException::new)
        );

    }

    @Override
    public List<HomeworkResponse> getAssignedHomework() {

        return buildResponseList(homeworkRepository.findAllByIsSubmittedFalseAndStudentEmailOrderByIdDesc(authenticationFacade.getUserEmail()));

    }

    @Override
    public List<HomeworkResponse> getSubmittedHomework() {

        return buildResponseList(homeworkRepository.findAllByIsSubmittedTrueAndStudentEmailOrderByIdDesc(authenticationFacade.getUserEmail()));
    }

    @Override
    public List<HomeworkResponse> getOrderedHomework() {
        return buildResponseList(homeworkRepository.findAllByTeacherEmailOrderByIdDesc(authenticationFacade.getUserEmail()));
    }

    @Override
    public void submitHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if(!homework.getStudentEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        homeworkRepository.save(homework.submitHomework());

    }

    @Override
    public void rejectHomework(Long homeworkId) {

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);
        
        if(!homework.getTeacherEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        homeworkRepository.save(homework.rejectHomework());
    }

    private List<HomeworkResponse> buildResponseList(List<Homework> homeworkList) {
        List<HomeworkResponse> homeworkResponses = new ArrayList<>();

        for(Homework work : homeworkList) {
            homeworkResponses.add(buildResponse(work));
        }

        return homeworkResponses;
    }

    private HomeworkResponse buildResponse(Homework homework) {
        return HomeworkResponse.builder()
                .homeworkId(homework.getId())
                .description(homework.getDescription())
                .endDate(homework.getTerm().getEndDate())
                .startDate(homework.getTerm().getStartDate())
                .isRejected(homework.getIsRejected())
                .major(homework.getMajor())
                .studentName(homework.getStudentEmail())
                .teacherName(homework.getTeacherEmail())
                .title(homework.getTitle())
                .build();
    }
}
