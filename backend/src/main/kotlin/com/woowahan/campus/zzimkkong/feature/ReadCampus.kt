package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import openapi.api.FindMapApi
import openapi.model.MapGetSingle
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReadCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : FindMapApi {

    override fun findAllMap(): ResponseEntity<List<MapGetSingle>> {
        val response: List<MapGetSingle> = campusRepository.findAll().map {
            val slackChannel = slackChannelRepository.findByCampusId(it.id)
            MapGetSingle(
                mapId = it.id.toInt(),
                mapName = it.name,
                mapDrawing = it.drawing,
                thumbnail = it.thumbnail,
                slackUrl = slackChannel.url
            )
        }.toList()

        return ResponseEntity.ok(response)
    }

    override fun findMap(mapId: Int): ResponseEntity<MapGetSingle> {
        val response: MapGetSingle = campusRepository.findById(mapId.toLong()).map {
            val slackChannel = slackChannelRepository.findByCampusId(it.id)
            MapGetSingle(
                mapId = it.id.toInt(),
                mapName = it.name,
                mapDrawing = it.drawing,
                thumbnail = it.thumbnail,
                slackUrl = slackChannel.url
            )
        }.orElseThrow { throw Exception("존재하지 않는 맵입니다.") }
        return ResponseEntity.ok(response)
    }
}
