package com.gramo.gramo.service.notice;

import com.gramo.gramo.entity.notice.Notice;
import com.gramo.gramo.entity.notice.NoticeRepository;
import com.gramo.gramo.exception.NoticeNotFoundException;
import com.gramo.gramo.factory.UserFactory;
import com.gramo.gramo.payload.request.CreateNoticeRequest;
import com.gramo.gramo.payload.response.NoticeDetailResponse;
import com.gramo.gramo.payload.response.NoticeListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final UserFactory userFactory;

    @Override
    public void createNotice(CreateNoticeRequest request) {
        noticeRepository.save(
                Notice.builder()
                        .title(request.getTitle())
                        .content(request.getContent())
                        .user(userFactory.getAuthUser())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public NoticeListResponse getNoticeList(int offset, int limitNum) {
//        int size = limitNum;
//        int page = offset /
//        return noticeRepository.
        return null;
    }

    @Override
    public NoticeDetailResponse getNotice(long noticeId) {
        return noticeRepository.findById(noticeId)
                .map(notice -> NoticeDetailResponse.builder()
                        .content(notice.getContent())
                        .name(userFactory.getUserName(notice.getUser().getEmail()))
                        .createdAt(notice.getCreatedAt())
                        .title(notice.getTitle())
                        .build())
                .orElseThrow(NoticeNotFoundException::new);
    }
}
