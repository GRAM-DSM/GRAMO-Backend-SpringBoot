package com.gramo.gramo.service.plan;

import com.gramo.gramo.entity.plan.Plan;
import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.mapper.PlanMapper;
import com.gramo.gramo.payload.request.PlanRequest;
import com.gramo.gramo.payload.response.PlanContentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService{

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;

    public List<PlanContentResponse> getPlan(LocalDate date) {

        List<Plan> plans = planRepository.findAllByDateOrderByIdDesc(date);
        List<PlanContentResponse> planContentResponses = new ArrayList<>();

        for(Plan plan : plans) {
            planContentResponses.add(planMapper.planToResponse(plan));
        }

        return planContentResponses;
    }

    public void postPlan(PlanRequest planRequest) {
        planRepository.save(planMapper.requestToPlan(planRequest));

    }

    public void deletePlan(Long planId) {
        planRepository.deleteById(planId);
    }

}
