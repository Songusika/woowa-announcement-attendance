package com.woowahan.campus.announcement.domain

data class MessageSendEvent(
    val announcement: Announcement,
) {
    val channelId = announcement.slackChannelId
    val author = announcement.author
    val content = announcement.content
}
