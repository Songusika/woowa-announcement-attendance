package com.woowahan.campus.announcement.domain

import org.springframework.data.repository.Repository

interface AnnouncementSlackChannelRepository : Repository<AnnouncementSlackChannel, Long> {

    fun findById(id: Long): AnnouncementSlackChannel?
}
