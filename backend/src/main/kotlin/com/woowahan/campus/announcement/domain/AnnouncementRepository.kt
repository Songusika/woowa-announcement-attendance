package com.woowahan.campus.announcement.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.jpa.repository.JpaRepository

interface AnnouncementRepository : JpaRepository<Announcement, Long> {

    fun save(announcement: Announcement): Announcement

    fun findByIdLessThanOrderByIdDesc(id: Long, pageRequest: PageRequest): Slice<Announcement>

    fun findByOrderByIdDesc(pageRequest: PageRequest): Slice<Announcement>
}
