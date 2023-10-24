package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import openapi.api.DeleteMapApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : DeleteMapApi {

    override fun removeMap(mapId: Int): ResponseEntity<Unit> {
        val campus = campusRepository.findById(mapId.toLong())
            .orElseThrow { IllegalArgumentException() }
        val slackChannel = slackChannelRepository.findByCampusId(campus.id)
        slackChannelRepository.delete(slackChannel)
        campusRepository.delete(campus)
        return ResponseEntity.noContent().build()
    }
}
