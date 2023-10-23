package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("SLACK_CHANNEL")
class SlackChannel (
    val url: String,
    val campusId: Long,
    @Id var id: Long = 0L,
)
