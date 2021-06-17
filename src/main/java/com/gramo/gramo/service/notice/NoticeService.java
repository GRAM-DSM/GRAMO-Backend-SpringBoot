package com.gramo.gramo.service.notice;

import com.gramo.gramo.payload.request.CreateNoticeRequest;
import com.gramo.gramo.payload.response.NoticeDetailResponse;
import com.gramo.gramo.payload.response.NoticeListResponse;

public interface NoticeService {
    void createNotice(CreateNoticeRequest request);

    NoticeListResponse getNoticeList(int offset, int limitNum);

    NoticeDetailResponse getNotice(long noticeId);
}
