package com.gramo.gramo.service.plan;

import com.gramo.gramo.payload.request.PlanRequest;
import com.gramo.gramo.payload.response.PlanContentResponse;

import java.time.LocalDate;
import java.util.List;

public interface PlanService {
    List<PlanContentResponse> getPlan(LocalDate date);

    void postPlan(PlanRequest planRequest);

    void deletePlan(Long planId);
}
