package com.gramo.gramo.payload.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanRequest {

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    @Size(max = 50)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    @FutureOrPresent
    private LocalDate date;

}
