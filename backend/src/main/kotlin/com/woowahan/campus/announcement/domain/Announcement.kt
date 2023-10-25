package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.support.BaseEntity

class Announcement(
    val title: String,
    val content: String,
    val author: String,
    val slackChannelId: Int,
    id: Long = 0L,
) : BaseEntity(id) {
    fun withId(id: Long): Announcement = Announcement(title, content, author, slackChannelId, id)
}
