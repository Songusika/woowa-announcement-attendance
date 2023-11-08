package com.woowahan.campus.announcement.domain

import org.springframework.data.repository.Repository

fun AnnouncementSlackChannelRepository.getById(id: Long) = findById(id)
    ?: throw IllegalArgumentException("슬랙 채널을 찾을 수 없습니다.")

interface AnnouncementSlackChannelRepository : Repository<AnnouncementSlackChannel, Long> {
    fun findById(id: Long): AnnouncementSlackChannel?
    fun existsById(id: Long): Boolean
    fun save(announcementSlackChannel: AnnouncementSlackChannel): AnnouncementSlackChannel
}
