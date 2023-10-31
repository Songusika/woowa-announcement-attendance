package com.woowahan.campus.announcement.domain

import org.springframework.data.repository.Repository

interface AnnouncementRepository : Repository<Announcement, Long> {

    fun save(announcement: Announcement): Announcement

    fun deleteById(id: Long)
}
