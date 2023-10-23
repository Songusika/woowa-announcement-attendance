package com.woowahan.campus.zzimkkong.domain

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
class SlackChannel(
    val url: String,
    val campusId: Long,
    @Id val id: Long = 0L,
) {
    fun withId(id: Long): SlackChannel = SlackChannel(url, campusId, id)
}
