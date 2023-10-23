package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.support.BaseRootEntity
import jakarta.persistence.Entity

@Entity
class Announcement(
    val title: String,
    val content: String,
    val author: String,
    val slackChannelId: Int,
    id: Long = 0L,
) : BaseRootEntity<Announcement>(id) {

    private fun publish(): Announcement {
        val event = MessageSendEvent(this)
        return this.andEvent(event)
    }

    companion object {
        fun create(title: String, content: String, author: String, slackChannelId: Int): Announcement {
            val announcement = Announcement(title, content, author, slackChannelId)
            return announcement.publish()
        }
    }
}
