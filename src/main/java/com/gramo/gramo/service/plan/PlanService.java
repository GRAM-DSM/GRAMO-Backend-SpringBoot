package com.gramo.gramo.service.plan;

import com.gramo.gramo.payload.request.PlanRequest;
import com.gramo.gramo.payload.response.PlanContentResponse;
import com.gramo.gramo.payload.response.PlanListResponse;

import java.time.LocalDate;
import java.util.List;

public interface PlanService {
    PlanListResponse getPlan(LocalDate date);

    void postPlan(PlanRequest planRequest);

    void deletePlan(Long planId);
}
