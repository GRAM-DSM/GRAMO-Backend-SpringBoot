package com.gramo.gramo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeContentResponse {

    private String title;

    private String content;

    private String userName;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:SS")
    private LocalDateTime createdAt;
}
