package com.woowahan.campus.announcement.domain

import com.woowahan.campus.announcement.support.BaseRootEntity
import jakarta.persistence.Entity

@Entity
class AnnouncementSlackChannel(
    val providerId: String,
    val name: String,
    id: Long = 0L,
) : BaseRootEntity<AnnouncementSlackChannel>(id)
