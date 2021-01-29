package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.PlanRequest;
import com.gramo.gramo.payload.response.PlanContentResponse;
import com.gramo.gramo.service.plan.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar/plan")
public class PlanController {

    private final PlanService planService;

    @GetMapping("/{planDate}")
    public List<PlanContentResponse> getPlan(@DateTimeFormat(pattern = "yyyy-MM-dd")
                                             @PathVariable LocalDate planDate) {
        return planService.getPlan(planDate);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void postPlan(@RequestBody PlanRequest planRequest) {
        planService.postPlan(planRequest);
    }

    @DeleteMapping("/{planId}")
    public void deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId);
    }

}
