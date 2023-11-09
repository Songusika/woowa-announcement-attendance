package com.woowahan.campus.zzimkkong.domain

import org.springframework.data.repository.Repository

fun SlackChannelRepository.getByCampusId(campusId: Long): SlackChannel =
    findByCampusId(campusId) ?: throw IllegalArgumentException()

interface SlackChannelRepository : Repository<SlackChannel, Long> {

    fun deleteByCampusId(campusId: Long)

    fun save(slackChannel: SlackChannel): SlackChannel

    fun findByCampusId(campusId: Long): SlackChannel?

    fun delete(slackChannel: SlackChannel)
}
