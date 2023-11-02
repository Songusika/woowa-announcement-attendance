package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.support.BaseRootEntity
import jakarta.persistence.Entity
import jakarta.persistence.Embedded

@Entity
class Announcement(
    @Embedded
    var title: Title,
    @Embedded
    var content: Content,
    @Embedded
    var author: Author,
    val slackChannelId: Int,
    id: Long = 0L,
) : BaseRootEntity<Announcement>(id) {
    fun withId(id: Long): Announcement = Announcement(title, content, author, slackChannelId, id)
    companion object {
        fun create(title: String, content: String, author: String, slackChannelId: Int): Announcement =
            Announcement(Title(title), Content(content), Author(author), slackChannelId)
    }
}
