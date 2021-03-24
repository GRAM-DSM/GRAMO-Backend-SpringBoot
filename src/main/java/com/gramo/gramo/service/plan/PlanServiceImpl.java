package com.gramo.gramo.service.plan;

import com.gramo.gramo.entity.plan.Plan;
import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.exception.LoginException;
import com.gramo.gramo.exception.PlanNotFoundException;
import com.gramo.gramo.payload.request.PlanRequest;
import com.gramo.gramo.payload.response.PlanContentResponse;
import com.gramo.gramo.security.auth.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService{

    private final PlanRepository planRepository;
    private final AuthenticationFacade authenticationFacade;

    public List<PlanContentResponse> getPlan(LocalDate date) {

        List<Plan> plans = planRepository.findAllByDateOrderByDateDesc(date);
        List<PlanContentResponse> planContentResponses = new ArrayList<>();

        for(Plan plan : plans) {
            planContentResponses.add(
                    PlanContentResponse.builder()
                            .title(plan.getTitle())
                            .description(plan.getDescription())
                            .planId(plan.getId())
                            .build()
            );
        }

        return planContentResponses;
    }

    public void postPlan(PlanRequest planRequest) {
        planRepository.save(
                Plan.builder()
                        .title(planRequest.getTitle())
                        .description(planRequest.getDescription())
                        .date(planRequest.getDate())
                        .build()
        );

    }

    public void deletePlan(Long planId) {

        planRepository.deleteById(planId);
    }

}
