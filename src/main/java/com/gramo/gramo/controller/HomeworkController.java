package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.MyHomeworkResponse;
import com.gramo.gramo.service.homework.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @GetMapping("/assign")
    public List<MyHomeworkResponse> getAssignedHomework() {
        return homeworkService.getAssignedHomework();
    }

    @GetMapping("/submit")
    public List<MyHomeworkResponse> getSubmittedHomework() {
        return homeworkService.getSubmittedHomework();
    }

    @GetMapping("/order")
    public List<MyHomeworkResponse> getOrderedHomework() {
        return homeworkService.getOrderedHomework();
    }

    @GetMapping("/{homeworkId}")
    public MyHomeworkResponse getHomework(@PathVariable Long homeworkId) {
        return homeworkService.getHomework(homeworkId);
    }

    @PatchMapping("/{homeworkId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void submitHomework(@PathVariable Long homeworkId) throws ExecutionException, InterruptedException {
        homeworkService.submitHomework(homeworkId);
    }

    @PatchMapping("/reject/{homeworkId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void rejectHomework(@PathVariable Long homeworkId) {
        homeworkService.rejectHomework(homeworkId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createHomework(@RequestBody @Valid HomeworkRequest request) throws ExecutionException, InterruptedException {
        homeworkService.saveHomework(request);
    }

    @DeleteMapping("/{homeworkId}")
    public void deleteHomework(@PathVariable Long homeworkId) throws ExecutionException, InterruptedException {
        homeworkService.deleteHomework(homeworkId);
    }

}
