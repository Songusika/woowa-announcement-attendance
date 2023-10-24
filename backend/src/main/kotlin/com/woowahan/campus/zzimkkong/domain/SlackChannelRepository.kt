package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.repository.CrudRepository

interface SlackChannelRepository : CrudRepository<SlackChannel, Long> {
    fun findByCampusId(campusId: Long): SlackChannel
}
