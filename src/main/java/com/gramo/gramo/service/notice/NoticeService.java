package com.gramo.gramo.service.notice;

import com.gramo.gramo.payload.response.NoticeListResponse;

public interface NoticeService {
    NoticeListResponse getNoticeList(Integer offSet, Integer limitNum);
}
