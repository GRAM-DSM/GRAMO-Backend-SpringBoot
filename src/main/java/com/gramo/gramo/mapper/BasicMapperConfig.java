package com.gramo.gramo.mapper;

import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

@MapperConfig(componentModel = "spring")      // 이게 있어야 Spring Bean에 등록.
// 매핑 안된 필드 무시는, build.gradle에서 활성화 되어 있어서 안해도 된다.
public interface BasicMapperConfig {
}
