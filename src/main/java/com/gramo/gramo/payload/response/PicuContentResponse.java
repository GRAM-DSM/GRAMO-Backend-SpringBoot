package com.gramo.gramo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PicuContentResponse {

    private Long picuId;

    private String description;

    private String userName;

}
