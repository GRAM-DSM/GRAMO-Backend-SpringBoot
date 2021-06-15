package com.gramo.gramo.service.picu;

import com.gramo.gramo.payload.request.PicuRequest;
import com.gramo.gramo.payload.response.PicuContentResponse;
import com.gramo.gramo.payload.response.PicuListResponse;

import java.time.LocalDate;
import java.util.List;

public interface PicuService {
    PicuListResponse getPicu(LocalDate date);

    void createPicu(PicuRequest request);

    void deletePicu(Long picuId);
}
