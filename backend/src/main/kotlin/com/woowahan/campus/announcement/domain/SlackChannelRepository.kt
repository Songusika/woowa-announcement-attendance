package com.woowahan.campus.announcement.domain

import org.springframework.data.repository.Repository

interface SlackChannelRepository : Repository<SlackChannel, Long> {

    fun existsById(id: Long): Boolean
}
