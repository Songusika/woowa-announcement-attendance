package com.woowahan.campus.announcement.domain

import org.springframework.data.jpa.repository.JpaRepository

interface AnnouncementRepository : JpaRepository<Announcement, Long> {

    fun save(announcement: Announcement): Announcement
}
