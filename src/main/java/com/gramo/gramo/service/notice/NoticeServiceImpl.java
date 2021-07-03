package com.gramo.gramo.service.notice;

import com.gramo.gramo.entity.notice.Notice;
import com.gramo.gramo.entity.notice.NoticeRepository;
import com.gramo.gramo.entity.user.UserRepository;
import com.gramo.gramo.exception.NoticeNotFoundException;
import com.gramo.gramo.factory.UserFactory;
import com.gramo.gramo.payload.request.CreateNoticeRequest;
import com.gramo.gramo.payload.response.NoticeDetailResponse;
import com.gramo.gramo.payload.response.NoticeListResponse;
import com.gramo.gramo.payload.response.NoticeResponse;
import com.gramo.gramo.service.notification.NotificationService;
import com.gramo.gramo.service.notification.enums.NotificationData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {

    private final NoticeRepository noticeRepository;
    private final NotificationService notificationService;
    private final UserFactory userFactory;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void createNotice(CreateNoticeRequest request) {

        notificationService.sendMultipleUser(userRepository.findAllByTokenNotNull(),
                NotificationData.NOTICE);

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
    public NoticeListResponse getNoticeList(Pageable pageable) {
        Page<Notice> notices = noticeRepository.findAllByOrderByCreatedAtDesc(pageable);
        return new NoticeListResponse(
                notices.getContent()
                        .stream().map(notice -> NoticeResponse.builder()
                        .content(notice.getContent())
                        .createdAt(notice.getCreatedAt())
                        .title(notice.getTitle())
                        .id(notice.getId())
                        .userName(userFactory.getUserName(notice.getUser().getEmail()))
                        .build()).collect(Collectors.toList()),
                notices.getTotalPages() > pageable.getPageNumber());
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
