package com.gramo.gramo.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeListResponse {

    private List<NoticeResponse> notice;

    @JsonProperty("next_page")
    private Boolean nextPage;
}
