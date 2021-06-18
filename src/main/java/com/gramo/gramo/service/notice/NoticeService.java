package com.gramo.gramo.service.notice;

import com.gramo.gramo.payload.request.CreateNoticeRequest;
import com.gramo.gramo.payload.response.NoticeDetailResponse;
import com.gramo.gramo.payload.response.NoticeListResponse;
import org.springframework.data.domain.Pageable;

public interface NoticeService {
    void createNotice(CreateNoticeRequest request);

    NoticeListResponse getNoticeList(Pageable pageable);

    NoticeDetailResponse getNotice(long noticeId);
}
