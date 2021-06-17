package com.gramo.gramo.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponse {

    private Long id;

    private String title;

    private String content;

    @JsonProperty("user_name")
    private String userName;

    @JsonProperty("created_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:SS")
    private LocalDateTime createdAt;

}
