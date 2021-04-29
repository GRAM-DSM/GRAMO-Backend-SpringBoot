package com.gramo.gramo.service.homework;

import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.MyHomeworkResponse;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface HomeworkService {
    void saveHomework(HomeworkRequest homeworkRequest) throws ExecutionException, InterruptedException;

    void deleteHomework(Long homeworkId) throws ExecutionException, InterruptedException;

    MyHomeworkResponse getHomework(Long homeworkId);

    List<MyHomeworkResponse> getAssignedHomework();

    List<MyHomeworkResponse> getSubmittedHomework();

    List<MyHomeworkResponse> getOrderedHomework();

    void submitHomework(Long homeworkId) throws ExecutionException, InterruptedException;

    void rejectHomework(Long homeworkId);
}
