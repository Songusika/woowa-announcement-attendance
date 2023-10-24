package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.support.BaseRootEntity

class Announcement(
    val title: String,
    val content: String,
    val author: String,
    val slackChannelId: Int,
    id: Long = 0L,
) : BaseRootEntity<Announcement>(id) {
    fun withId(id: Long): Announcement = Announcement(title, content, author, slackChannelId, id)

    fun write(): Announcement {
        val event = MessageSendEvent(this)
        return this.andEvent(event)
    }
}
