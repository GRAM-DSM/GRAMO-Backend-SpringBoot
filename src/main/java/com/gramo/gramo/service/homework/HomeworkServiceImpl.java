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
        notificationService.sendNotification(
                userFactory.getUser(homeworkRequest.getStudentEmail()), userFactory.getAuthUser() + "님이 " +
                        userFactory.getUser(homeworkRequest.getStudentEmail()).getName() +
                        "님에게 숙제를 냈습니다.");

        homeworkRepository.save(homeworkMapper.toHomework(homeworkRequest, userFactory.getAuthUser().getEmail()));
    }

    @Override
    public void deleteHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);
        if (!authenticationFacade.getUserEmail().equals(homework.getTeacherEmail())) {
            throw new PermissionMismatchException();
        }

        notificationService.sendNotification(userFactory.getUser(homework.getStudentEmail()),
                userFactory.getAuthUser() + "님이 " + homework.getTitle() + " 숙제를 삭제했습니다.");

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

        notificationService.sendNotification(userFactory.getUser(homework.getStudentEmail()),
                userFactory.getAuthUser().getName() + "님이 숙제를 제출했습니다.");

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

        notificationService.sendNotification(userFactory.getUser(homework.getStudentEmail()),
                userFactory.getAuthUser().getName() + "님이 숙제를 반환했습니다.");
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
