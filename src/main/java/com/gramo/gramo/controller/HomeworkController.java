package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.HomeworkResponse;
import com.gramo.gramo.service.homework.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/homework")
public class HomeworkController {

    private final HomeworkService homeworkService;

    @GetMapping("/assign")
    public List<HomeworkResponse> getAssignedHomework() {
        return homeworkService.getAssignedHomework();
    }

    @GetMapping("/submit")
    public List<HomeworkResponse> getSubmittedHomework() {
        return homeworkService.getSubmittedHomework();
    }

    @GetMapping("/order")
    public List<HomeworkResponse> getOrderedHomework() {
        return homeworkService.getOrderedHomework();
    }

    @GetMapping("/{homeworkId}")
    public HomeworkResponse getHomework(@PathVariable Long homeworkId) {
        return homeworkService.getHomework(homeworkId);
    }

    @PatchMapping("/{homeworkId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void submitHomework(@PathVariable Long homeworkId) {
        homeworkService.submitHomework(homeworkId);
    }

    @PatchMapping("/reject/{homeworkId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void rejectHomework(@PathVariable Long homeworkId) {
        homeworkService.rejectHomework(homeworkId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createHomework(@RequestBody HomeworkRequest request) {
        homeworkService.saveHomework(request);
    }

    @DeleteMapping("/{homeworkId}")
    public void deleteHomework(@PathVariable Long homeworkId) {
        homeworkService.deleteHomework(homeworkId);
    }

}
