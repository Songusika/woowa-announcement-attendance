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
}
