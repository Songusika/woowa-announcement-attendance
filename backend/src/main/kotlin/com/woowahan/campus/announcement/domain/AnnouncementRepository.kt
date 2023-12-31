package com.woowahan.campus.announcement.domain

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Slice
import org.springframework.data.repository.Repository

fun AnnouncementRepository.getById(id: Long) = findById(id)
    ?: throw IllegalArgumentException("존재하지 않는 announcement입니다.")

interface AnnouncementRepository : Repository<Announcement, Long> {

    fun findById(id: Long): Announcement?

    fun save(announcement: Announcement): Announcement

    fun findAllBy(pageRequest: PageRequest): Page<Announcement>

    fun deleteById(id: Long)

    fun existsById(id: Long): Boolean

    fun findByIdLessThanOrderByIdDesc(id: Long, pageRequest: PageRequest): Slice<Announcement>

    fun findByOrderByIdDesc(pageRequest: PageRequest): Slice<Announcement>
}
