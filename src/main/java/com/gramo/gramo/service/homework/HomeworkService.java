package com.gramo.gramo.service.homework;

import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.HomeworkResponse;

import java.util.List;

public interface HomeworkService {
    void saveHomework(HomeworkRequest homeworkRequest);

    void deleteHomework(Long homeworkId);

    HomeworkResponse getHomework(Long homeworkId);

    List<HomeworkResponse> getAssignedHomework();

    List<HomeworkResponse> getSubmittedHomework();

    List<HomeworkResponse> getOrderedHomework();

    void submitHomework(Long homeworkId);

    void rejectHomework(Long homeworkId);

}
