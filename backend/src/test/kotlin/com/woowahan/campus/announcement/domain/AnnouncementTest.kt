package com.woowahan.campus.announcement.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class AnnouncementTest : StringSpec({

    "공지 사항을 생성하면 이벤트를 발행한다" {
        var announcement = Announcement.create("제목", "내용", "작성자", 1)
        announcement.events.size shouldBe 1
    }
})
