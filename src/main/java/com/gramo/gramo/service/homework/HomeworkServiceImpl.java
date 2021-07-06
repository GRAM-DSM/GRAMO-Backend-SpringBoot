package com.gramo.gramo.service.homework;

import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.homework.HomeworkRepository;
import com.gramo.gramo.exception.HomeworkNotFoundException;
import com.gramo.gramo.exception.NotAssignedException;
import com.gramo.gramo.exception.PermissionMismatchException;
import com.gramo.gramo.factory.UserFactory;
import com.gramo.gramo.mapper.HomeworkMapper;
import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.MyHomeworkResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import com.gramo.gramo.service.notification.NotificationService;
import com.gramo.gramo.service.notification.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final AuthenticationFacade authenticationFacade;
    private final HomeworkRepository homeworkRepository;
    private final NotificationService notificationService;

    private final HomeworkMapper homeworkMapper;
    private final UserFactory userFactory;

    @Override
    @Transactional
    public void saveHomework(HomeworkRequest homeworkRequest) {
        notificationService.sendNotification(userFactory.getUser(homeworkRequest.getStudentEmail()), NotificationType.CREATE_HOMEWORK,
                homeworkRequest.getTitle());

        homeworkRepository.save(homeworkMapper.toHomework(homeworkRequest, userFactory.getAuthUser().getEmail()));
    }

    @Override
    public void deleteHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);
        if (!authenticationFacade.getUserEmail().equals(homework.getTeacherEmail())) {
            throw new PermissionMismatchException();
        }

        homeworkRepository.delete(homework);
    }

    @Override
    public MyHomeworkResponse getHomework(Long homeworkId) {
        return buildResponse(homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new));

    }

    @Override
    public List<MyHomeworkResponse> getAssignedHomework() {
        return buildResponseList(homeworkRepository
                .findAllByStatusIsSubmittedFalseAndStudentEmailOrderByIdDesc(authenticationFacade.getUserEmail()));
    }

    @Override
    public List<MyHomeworkResponse> getSubmittedHomework() {
        return buildResponseList(homeworkRepository
                .findAllByStatusIsSubmittedTrueAndStudentEmailOrderByIdDesc(authenticationFacade.getUserEmail()));
    }

    @Override
    public List<MyHomeworkResponse> getOrderedHomework() {
        return buildResponseList(homeworkRepository
                .findAllByTeacherEmailOrderByIdDesc(authenticationFacade.getUserEmail()));
    }

    @Override
    @Transactional
    public void submitHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if (!homework.getStudentEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        if (homework.getStatus().getIsSubmitted())
            throw new NotAssignedException();

        homework.getStatus().submitHomework();

        notificationService.sendNotification(userFactory.getUser(homework.getTeacherEmail()), NotificationType.SUBMIT_HOMEWORK, homework.getTitle());

    }

    @Override
    @Transactional
    public void rejectHomework(Long homeworkId) {

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if (!homework.getTeacherEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        if (!homework.getStatus().getIsSubmitted()) {
            throw new NotAssignedException();
        }

        homework.getStatus().rejectHomework();

        notificationService.sendNotification(userFactory.getUser(homework.getStudentEmail()), NotificationType.REJECT_HOMEWORK, homework.getTitle());
    }

    private List<MyHomeworkResponse> buildResponseList(List<Homework> homeworkList) {
        List<MyHomeworkResponse> myHomeworkResponse = new ArrayList<>();
        homeworkList.forEach(homework -> myHomeworkResponse.add(buildResponse(homework)));
        return myHomeworkResponse;
    }

    private MyHomeworkResponse buildResponse(Homework homework) {
        return homeworkMapper.toHomeworkResponse(homework, userFactory.getUserName(homework.getStudentEmail()),
                userFactory.getUserName(homework.getTeacherEmail()), homework.getTeacherEmail().equals(authenticationFacade.getUserEmail()));
    }

}
