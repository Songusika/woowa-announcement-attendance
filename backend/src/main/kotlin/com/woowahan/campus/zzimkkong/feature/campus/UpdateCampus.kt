package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.domain.Campus
import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannel
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import com.woowahan.campus.zzimkkong.domain.getByCampusId
import com.woowahan.campus.zzimkkong.domain.getById
import openapi.api.UpdateMapApi
import openapi.model.MapPut
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : UpdateMapApi {

    @Transactional
    override fun updateMap(mapId: Int, mapPut: MapPut): ResponseEntity<Unit> {
        val campus = campusRepository.getById(mapId.toLong())
        val slackChannel = slackChannelRepository.getByCampusId(campus.id)

        campusRepository.save(
            Campus(
                name = mapPut.mapName,
                drawing = mapPut.mapDrawing,
                thumbnail = mapPut.thumbnail,
                id = campus.id,
            )
        )
        slackChannelRepository.save(SlackChannel(id = slackChannel.id, campusId = campus.id, url = mapPut.slackUrl))
        return ResponseEntity.ok().build()
    }
}
