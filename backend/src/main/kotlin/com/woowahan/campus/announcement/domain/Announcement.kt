package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.support.BaseRootEntity
import jakarta.persistence.Embedded
import jakarta.persistence.Entity

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

    fun update(title: String, content: String, author: String) {
        require(title.isNotBlank() && content.isNotBlank() && author.isNotBlank()) {
            "공지의 제목, 내용, 작성자는 빈 칸으로 입력할 수 없습니다."
        }
        this.title = Title(title)
        this.content = Content(content)
        require(this.author == Author(author)) {
            "공지의 작성자만이 공지를 수정할 수 있습니다."
        }
        this.author = Author(author)
    }

    private fun publish(): Announcement {
        val event = MessageSendEvent(this)
        return this.andEvent(event)
    }

    companion object {
        fun create(title: String, content: String, author: String, slackChannelId: Int): Announcement =
            Announcement(Title(title), Content(content), Author(author), slackChannelId)
    }
}
