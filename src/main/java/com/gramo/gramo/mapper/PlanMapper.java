package com.gramo.gramo.mapper;

import com.gramo.gramo.entity.plan.Plan;
import com.gramo.gramo.payload.request.PlanRequest;
import com.gramo.gramo.payload.response.PlanContentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BasicMapperConfig.class)
public interface PlanMapper {

    @Mapping(source = "plan.id", target = "planId")
    PlanContentResponse planToResponse(Plan plan);

    Plan requestToPlan(PlanRequest request);
}
