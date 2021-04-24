package com.gramo.gramo.service.homework;

import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.homework.HomeworkRepository;
import com.gramo.gramo.entity.homework.embedded.Status;
import com.gramo.gramo.entity.homework.embedded.Term;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.HomeworkNotFoundException;
import com.gramo.gramo.exception.NotAssignedException;
import com.gramo.gramo.exception.PermissionMismatchException;
import com.gramo.gramo.exception.UserNotFoundException;
import com.gramo.gramo.mapper.HomeworkMapper;
import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.MyHomeworkResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkServiceImpl implements HomeworkService {

    private final AuthenticationFacade authenticationFacade;
    private final HomeworkRepository homeworkRepository;
    private final UserRepository userRepository;

    private final HomeworkMapper homeworkMapper;

    @Override
    public void saveHomework(HomeworkRequest homeworkRequest) {
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

        if(homework.getStatus().getIsSubmitted() == true)
            throw new NotAssignedException();

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

        if(homework.getStatus().getIsSubmitted() == false) {
            throw new NotAssignedException();
        }

        homework.getStatus().rejectHomework();
    }

    private List<MyHomeworkResponse> buildResponseList(List<Homework> homeworkList) {
        List<MyHomeworkResponse> myHomeworkResponse = new ArrayList<>();
        homeworkList.forEach(homework -> myHomeworkResponse.add(buildResponse(homework)));
        return myHomeworkResponse;
    }

    private MyHomeworkResponse buildResponse(Homework homework) {
        return homeworkMapper.toHomeworkResponse(homework,
                getUser(homework.getStudentEmail()).getName(), getUser(homework.getTeacherEmail()).getName());
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }
}
