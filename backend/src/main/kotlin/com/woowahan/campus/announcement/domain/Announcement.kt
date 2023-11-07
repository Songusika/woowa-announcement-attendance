package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.exception.AuthorizationException
import com.woowahan.campus.announcement.support.BaseRootEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

@Entity
class Announcement private constructor(
    @Embedded
    var title: Title,
    @Embedded
    var content: Content,
    @Embedded
    var author: Author,
    val slackChannelId: Long,
    id: Long = 0L,
) : BaseRootEntity<Announcement>(id) {

    fun update(title: Title, content: Content, author: Author) {
        if (this.author != author) {
            throw AuthorizationException("공지 작성자만이 공지를 수정할 수 있습니다.")
        }
        this.title = title
        this.content = content
        this.author = author
        this.publish()
    }

    private fun publish(): Announcement {
        val event = MessageSendEvent(this)
        return this.andEvent(event)
    }

    companion object {
        fun create(title: Title, content: Content, author: Author, slackChannelId: Long): Announcement {
            return Announcement(title, content, author, slackChannelId).publish()
        }
    }
}
