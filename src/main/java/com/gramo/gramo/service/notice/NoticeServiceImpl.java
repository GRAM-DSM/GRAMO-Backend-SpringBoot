package com.gramo.gramo.service.notice;

import com.gramo.gramo.entity.notice.Notice;
import com.gramo.gramo.entity.notice.NoticeRepository;
import com.gramo.gramo.payload.response.NoticeContentResponse;
import com.gramo.gramo.payload.response.NoticeListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;

    @Override
    public NoticeListResponse getNoticeList(Integer offSet, Integer limitNum) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Notice> notices = noticeRepository.findAllBy(pageable);
        List<NoticeContentResponse> noticeContentResponses = new ArrayList<>();
        notices.forEach(
                notice -> noticeContentResponses.add(
                        NoticeContentResponse.builder()
                                .content(notice.getContent())
                                .createdAt(notice.getCreatedAt())
                                .title(notice.getTitle())
                                .userName(notice.getUserName())
                                .build())
        );
        return new NoticeListResponse(noticeContentResponses);
    }
}
