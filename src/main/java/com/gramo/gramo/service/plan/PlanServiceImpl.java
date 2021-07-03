package com.gramo.gramo.service.plan;

import com.gramo.gramo.entity.plan.PlanRepository;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.mapper.PlanMapper;
import com.gramo.gramo.payload.request.PlanRequest;
import com.gramo.gramo.payload.response.PlanContentResponse;
import com.gramo.gramo.payload.response.PlanListResponse;
import com.gramo.gramo.service.notification.NotificationService;
import com.gramo.gramo.service.notification.enums.NotificationData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanMapper planMapper;
    private final NotificationService notificationService;

    @Override
    public PlanListResponse getPlan(LocalDate date) {
        List<PlanContentResponse> planContentResponses = planRepository.findAllByDateOrderByIdDesc(date)
                .stream().map(planMapper::planToResponse)
                .collect(Collectors.toList());
        return new PlanListResponse(planContentResponses);
    }

    @Override
    @Transactional
    public void postPlan(PlanRequest planRequest) {
        notificationService.sendMultipleUser(userRepository.findAllByTokenNotNull(), NotificationData.CREATE_PLAN);
        planRepository.save(planMapper.requestToPlan(planRequest));

    }

    @Override
    public void deletePlan(Long planId) {
        planRepository.deleteById(planId);
    }

}
