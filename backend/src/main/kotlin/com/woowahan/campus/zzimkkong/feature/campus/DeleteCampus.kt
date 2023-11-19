package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import openapi.api.DeleteMapApi
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class DeleteCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : DeleteMapApi {

    @Transactional
    override fun removeMap(mapId: Int): ResponseEntity<Unit> {
        slackChannelRepository.deleteByCampusId(mapId.toLong())
        campusRepository.deleteById(mapId.toLong())
        return ResponseEntity.noContent().build()
    }
}
