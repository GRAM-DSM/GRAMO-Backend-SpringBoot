package com.gramo.gramo.mapper;

import com.gramo.gramo.entity.homework.Homework;
import com.gramo.gramo.entity.user.User;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.payload.request.HomeworkRequest;
import com.gramo.gramo.payload.response.MyHomeworkResponse;
import lombok.RequiredArgsConstructor;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(config = BasicMapperConfig.class, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HomeworkMapper {
//
//    HomeworkMapper INSTANCE = Mappers.getMapper(HomeworkMapper.class);      // 이 정보로 클래스를 생성

    @Mapping(source = "homework.id", target = "homeworkId")      // entity의 id와 response의 homeworkId와 매핑
    @Mapping(source = "homework.term.startDate", target = "startDate")      // entity의 Embedded 타입인 startDate와 response의 startDate 매핑
    @Mapping(source = "homework.term.endDate", target = "endDate")
    @Mapping(source = "homework.status.isRejected", target = "isRejected")  // embedded 타입인데, 모두 소문자를 사용해야 한다.
    @Mapping(source = "homework.description", target = "description")
    MyHomeworkResponse toHomeworkResponse(Homework homework, String studentName, String teacherName);  // 이런 식으로 여러 파라미터를 사용할 수 있다.
    // 만약 이미 존재하는 인스턴스를 매핑시켜 주고 싶다면, @MappingTarget을 사용하면 된다.

    @Mapping(source = "request.endDate", target = "term.endDate")
    Homework toHomework(HomeworkRequest request, String teacherEmail);
}
