package com.gramo.gramo.service.homework;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.homework.HomeworkRepository;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.*;
import com.gramo.gramo.factory.UserFactory;
import com.gramo.gramo.mapper.HomeworkMapper;
import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.request.NotificationRequest;
import com.gramo.gramo.payload.response.MyHomeworkResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private String path = "gramo-d19da-firebase-adminsdk-tka95-218dd64be4";

    private final AuthenticationFacade authenticationFacade;
    private final HomeworkRepository homeworkRepository;

    private final HomeworkMapper homeworkMapper;
    private final UserFactory userFactory;

    @Override
    public void saveHomework(HomeworkRequest homeworkRequest) {
//        sendNotification(buildRequest(userFactory.getAuthUser() + "님이 " +
//                userFactory.getUser(homeworkRequest.getStudentEmail()).getName() +
//                "님에게 숙제를 냈습니다.", userFactory.getUser(homeworkRequest.getStudentEmail())));

        homeworkRepository.save(
                homeworkMapper.toHomework(homeworkRequest, authenticationFacade.getUserEmail())
        );
    }

    @Override
    public void deleteHomework(Long homeworkId) {
        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);
        if(!authenticationFacade.getUserEmail().equals(homework.getTeacherEmail())) {
            throw new PermissionMismatchException();
        }
//        sendNotification(buildRequest(userFactory.getAuthUser() + "님이 " + homework.getTitle() + " 숙제를 삭제했습니다.",
//                userFactory.getUser(homework.getStudentEmail())));
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

        if(!homework.getStudentEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        if(homework.getStatus().getIsSubmitted())
            throw new NotAssignedException();

        homework.getStatus().submitHomework();

//        sendNotification(buildRequest(userFactory.getAuthUser() + "님이 숙제를 제출했습니다.",
//                userFactory.getUser(homework.getStudentEmail())));

    }

    @Override
    @Transactional
    public void rejectHomework(Long homeworkId) {

        Homework homework = homeworkRepository.findById(homeworkId)
                .orElseThrow(HomeworkNotFoundException::new);

        if(!homework.getTeacherEmail().equals(authenticationFacade.getUserEmail())) {
            throw new PermissionMismatchException();
        }

        if(!homework.getStatus().getIsSubmitted()) {
            throw new NotAssignedException();
        }

        homework.getStatus().rejectHomework();

//        sendNotification(buildRequest(userFactory.getAuthUser() + "님이 숙제를 제출했습니다.",
//                userFactory.getUser(homework.getStudentEmail())));
    }

    private List<MyHomeworkResponse> buildResponseList(List<Homework> homeworkList) {
        List<MyHomeworkResponse> myHomeworkResponse = new ArrayList<>();
        homeworkList.forEach(homework -> myHomeworkResponse.add(buildResponse(homework)));
        return myHomeworkResponse;
    }

    private MyHomeworkResponse buildResponse(Homework homework) {
        return homeworkMapper.toHomeworkResponse(homework, userFactory.getUser(homework.getStudentEmail()).getName(),
                userFactory.getUser(homework.getTeacherEmail()).getName(), homework.getTeacherEmail().equals(authenticationFacade.getUserEmail()));
    }

    private NotificationRequest buildRequest(String content, User user) {
        return NotificationRequest.builder()
                .body(content)
                .title("GRAMO HOMEWORK")
                .token(user.getToken())
                .build();
    }

    @PostConstruct
    public void init() {
        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(path).getInputStream());
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(googleCredentials)
                    .build();

            if(FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(NotificationRequest request) {
        try {
            Message message = Message.builder()
                    .setToken(request.getToken())
                    .setNotification(Notification.builder()
                            .setBody(request.getBody())
                            .setTitle(request.getTitle())
                            .build())
                    .build();
            FirebaseMessaging.getInstance().sendAsync(message).get();
        } catch (Exception e) {
            throw new SendNotificationFailed();
        }
    }
}
