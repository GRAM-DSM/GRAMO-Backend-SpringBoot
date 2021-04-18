package com.gramo.gramo.service.homework;

import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.MyHomeworkResponse;

import java.util.List;

public interface HomeworkService {
    void saveHomework(HomeworkRequest homeworkRequest);

    void deleteHomework(Long homeworkId);

    MyHomeworkResponse getHomework(Long homeworkId);

    List<MyHomeworkResponse> getAssignedHomework();

    List<MyHomeworkResponse> getSubmittedHomework();

    List<MyHomeworkResponse> getOrderedHomework();

    void submitHomework(Long homeworkId);

    void rejectHomework(Long homeworkId);

}
