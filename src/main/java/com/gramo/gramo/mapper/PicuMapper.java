package com.gramo.gramo.mapper;

import com.gramo.gramo.entity.picu.Picu;
import com.gramo.gramo.payload.request.PicuRequest;
import com.gramo.gramo.payload.response.PicuContentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BasicMapperConfig.class)
public interface PicuMapper {

    @Mapping(source = "picu.id", target = "picuId")
    PicuContentResponse toResponse(Picu picu, String userName);

    @Mapping(target = "id", ignore = true)
    Picu toPicu(PicuRequest request, String userEmail);

}
