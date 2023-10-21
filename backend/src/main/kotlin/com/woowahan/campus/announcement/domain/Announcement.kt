package com.woowahan.campus.announcement.domain

class Announcement(
    val title: String,
    val content: String,
    val author: String,
    val slackChannelId: Int,
    id: Long = 0L,
) : BaseEntity(id) {
    fun withId(id: Long): Announcement = Announcement(title, content, author, slackChannelId, id)
}
