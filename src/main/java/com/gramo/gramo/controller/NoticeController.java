package com.gramo.gramo.controller;

import com.gramo.gramo.entity.notice.NoticeRepository;
import com.gramo.gramo.payload.response.NoticeListResponse;
import com.gramo.gramo.service.notice.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;


    @GetMapping("/{offset}/{limitNum}")
    public NoticeListResponse getNoticeList(@PathVariable Integer limitNum, @PathVariable Integer offset) {
        return noticeService.getNoticeList(offset, limitNum);
    }
}
