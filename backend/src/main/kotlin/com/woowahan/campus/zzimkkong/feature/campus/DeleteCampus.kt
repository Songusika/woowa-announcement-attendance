package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import com.woowahan.campus.zzimkkong.domain.getByCampusId
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.DeleteMapApi
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : DeleteMapApi {

    override fun removeMap(mapId: Int): ResponseEntity<Unit> {
        val campus = campusRepository.getById(mapId.toLong())
        val slackChannel = slackChannelRepository.getByCampusId(campus.id)
        slackChannelRepository.delete(slackChannel)
        campusRepository.delete(campus)
        return ResponseEntity.noContent().build()
    }
}
