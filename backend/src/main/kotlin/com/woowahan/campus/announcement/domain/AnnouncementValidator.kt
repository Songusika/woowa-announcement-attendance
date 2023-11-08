package com.woowahan.campus.announcement.domain

import org.springframework.stereotype.Component

@Component
class AnnouncementValidator(
    private val announcementSlackChannelRepository: AnnouncementSlackChannelRepository
) {

    fun validate(
        title: String,
        content: String,
        author: String,
        slackChannelId: Long,
    ) {
        require(announcementSlackChannelRepository.existsById(slackChannelId)) {
            "존재하지 않는 슬랙 채널입니다."
        }
    }
}
