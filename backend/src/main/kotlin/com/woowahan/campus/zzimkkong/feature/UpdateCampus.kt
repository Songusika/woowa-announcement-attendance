package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.domain.Campus
import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannel
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import openapi.api.UpdateMapApi
import openapi.model.MapPut
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UpdateCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : UpdateMapApi {

    override fun updateMap(mapId: Int, mapPut: MapPut): ResponseEntity<Unit> {
        val campus = campusRepository.findById(mapId.toLong())
            .orElseThrow { IllegalArgumentException() }
        val slackChannel = slackChannelRepository.findByCampusId(campus.id)

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
