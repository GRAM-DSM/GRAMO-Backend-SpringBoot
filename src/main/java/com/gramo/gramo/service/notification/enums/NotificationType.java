package com.gramo.gramo.service.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationType {

    NOTICE("공지사항이 올라왔습니다.", "notice"),
    CREATE_HOMEWORK("새로운 숙제가 할당되었습니다.", "homework"),
    SUBMIT_HOMEWORK("후배의 숙제가 제출되었습니다.", "homework"),
    REJECT_HOMEWORK("숙제가 반환되었습니다.", "homework"),
    CREATE_PICU("Picu에 새로운 글이 등록되었습니다.", "picu"),
    CREATE_PLAN("Plan에 새로운 글이 등록되었습니다.", "plan");

    private final String title;

    private final String type;
}
