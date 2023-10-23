package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.support.BaseRootEntity

class SlackChannel(
    val providerId: String,
    val name: String,
    id: Long = 0L,
) : BaseRootEntity<SlackChannel>(id) {
}
