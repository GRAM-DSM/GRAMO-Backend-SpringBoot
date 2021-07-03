package com.gramo.gramo.service.notification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationData {

    NOTICE("새로운 공지사항이 올라왔습니다!", "notice"),
    CREATE_HOMEWORK("새로운 숙제가 할당되었습니다.", "homework"),
    SUBMIT_HOMEWORK("후배의 숙제가 제출되었습니다. 확인해 주세요!", "homework"),
    REJECT_HOMEWORK("숙제가 반환되었습니다!", "homework"),
    DELETE_HOMEWORK("숙제가 삭제되었습니다!", "homework"),
    CREATE_PICU("Picu에 글이 등록되었습니다!", "picu"),
    CREATE_PLAN("새로운 특별한 일이 생겼습니다!", "plan");

    private final String message;

    private final String type;
}
