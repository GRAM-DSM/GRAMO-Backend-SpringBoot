package com.gramo.gramo.controller;

import com.gramo.gramo.payload.request.CreateNoticeRequest;
import com.gramo.gramo.payload.response.NoticeDetailResponse;
import com.gramo.gramo.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/{noticeId}")
    public NoticeDetailResponse getNotice(@PathVariable long noticeId) {
        return noticeService.getNotice(noticeId);
    }

    @PostMapping
    public void createNotice(@RequestBody CreateNoticeRequest request) {
        noticeService.createNotice(request);
    }
}
