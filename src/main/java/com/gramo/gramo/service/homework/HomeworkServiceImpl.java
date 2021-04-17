package com.gramo.gramo.service.homework;

import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.homework.HomeworkRepository;
import com.gramo.gramo.entity.homework.embedded.Status;
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

import javax.transaction.Transactional;
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

        Status status = new Status();

        homeworkRepository.save(
                Homework.builder()
                        .teacherEmail(authenticationFacade.getUserEmail())
                        .description(homeworkRequest.getDescription())
                        .major(homeworkRequest.getMajor())
                        .studentEmail(homeworkRequest.getStudentEmail())
                        .title(homeworkRequest.getTitle())
                        .term(term)
                        .status(status)
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

        return buildResponseList(homeworkRepository
                .findAllByStatusIsSubmittedFalseAndStudentEmailOrderByIdDesc(authenticationFacade.getUserEmail()));

    }

    @Override
    public List<HomeworkResponse> getSubmittedHomework() {

        return buildResponseList(homeworkRepository
                .findAllByStatusIsSubmittedTrueAndStudentEmailOrderByIdDesc(authenticationFacade.getUserEmail()));
    }

    @Override
    public List<HomeworkResponse> getOrderedHomework() {
        return buildResponseList(homeworkRepository
                .findAllByTeacherEmailOrderByIdDesc(authenticationFacade.getUserEmail()));
    }

    @Override
    @Transactional
    public void submitHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if(!homework.getStudentEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        homework.getStatus().submitHomework();

    }

    @Override
    @Transactional
    public void rejectHomework(Long homeworkId) {

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);
        
        if(!homework.getTeacherEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        homework.getStatus().rejectHomework();
    }

    private List<HomeworkResponse> buildResponseList(List<Homework> homeworkList) {
        List<HomeworkResponse> homeworkResponses = new ArrayList<>();
        homeworkList.forEach(homework -> homeworkResponses.add(buildResponse(homework)));
        return homeworkResponses;
    }

    private HomeworkResponse buildResponse(Homework homework) {
        return HomeworkResponse.builder()
                .homeworkId(homework.getId())
                .description(homework.getDescription())
                .endDate(homework.getTerm().getEndDate())
                .startDate(homework.getTerm().getStartDate())
                .isRejected(homework.getStatus().getIsRejected())
                .major(homework.getMajor())
                .studentName(homework.getStudentEmail())
                .teacherName(homework.getTeacherEmail())
                .title(homework.getTitle())
                .build();
    }
}
