package com.woowahan.campus.zzimkkong.feature.campus

import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import com.woowahan.campus.zzimkkong.domain.getByCampusId
import com.woowahan.campus.zzimkkong.domain.getById
import com.woowahan.campus.zzimkkong.domain.getByName
import openapi.api.FindMapApi
import openapi.model.MapGetAll
import openapi.model.MapGetSingle
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReadCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : FindMapApi {

    override fun findSharingMap(sharingMapId: String): ResponseEntity<MapGetSingle> {
        val campus = campusRepository.getByName(sharingMapId)
        val slackChannel = slackChannelRepository.getByCampusId(campus.id)
        val response = MapGetSingle(
            mapId = campus.id.toInt(),
            mapName = campus.name,
            mapDrawing = campus.drawing,
            thumbnail = campus.thumbnail,
            slackUrl = slackChannel.url
        )
        return ResponseEntity.ok(response)
    }

    override fun findAllMap(): ResponseEntity<MapGetAll> {
        val response: List<MapGetSingle> = campusRepository.findAll().map {
            val slackChannel = slackChannelRepository.getByCampusId(it.id)
            MapGetSingle(
                mapId = it.id.toInt(),
                mapName = it.name,
                mapDrawing = it.drawing,
                thumbnail = it.thumbnail,
                slackUrl = slackChannel.url
            )
        }.toList()

        return ResponseEntity.ok(MapGetAll(response))
    }

    override fun findMap(mapId: Int): ResponseEntity<MapGetSingle> {
        val campus = campusRepository.getById(mapId.toLong())
        val slackChannel = slackChannelRepository.getByCampusId(campus.id)
        val response = MapGetSingle(
            mapId = campus.id.toInt(),
            mapName = campus.name,
            mapDrawing = campus.drawing,
            thumbnail = campus.thumbnail,
            slackUrl = slackChannel.url
        )
        return ResponseEntity.ok(response)
    }
}
