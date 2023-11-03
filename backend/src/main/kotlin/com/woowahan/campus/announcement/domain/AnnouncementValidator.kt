package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.exception.SlackChannelNotFoundException
import org.springframework.stereotype.Component

@Component
class AnnouncementValidator(
    private val announcementSlackChannelRepository: AnnouncementSlackChannelRepository
) {

    fun validate(
        title: String,
        content: String,
        author: String,
        slackChannelId: Int,
    ) {
        validateExistsSlackChannel(slackChannelId.toLong())
    }

    fun validateExistsSlackChannel(slackChannelId: Long) {
        if (announcementSlackChannelRepository.existsById(slackChannelId)) {
            throw SlackChannelNotFoundException("존재하지 않는 슬랙 채널입니다.")
        }
    }
}
