package com.woowahan.campus.zzimkkong.feature

import com.woowahan.campus.zzimkkong.domain.Campus
import com.woowahan.campus.zzimkkong.domain.CampusRepository
import com.woowahan.campus.zzimkkong.domain.SlackChannel
import com.woowahan.campus.zzimkkong.domain.SlackChannelRepository
import openapi.api.MapApi
import openapi.model.MapPost
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class CreateCampus(
    val campusRepository: CampusRepository,
    val slackChannelRepository: SlackChannelRepository,
) : MapApi {

    @Transactional
    override fun createMap(mapPost: MapPost): ResponseEntity<Unit> {
        val savedCampus = campusRepository.save(Campus(mapPost.mapName, mapPost.mapDrawing, mapPost.thumbnail))
        val savedSlackChannel = slackChannelRepository.save(SlackChannel(mapPost.slackUrl, savedCampus.id))
        return ResponseEntity.created(URI.create("/api/maps/${savedCampus.id}")).build()
    }
}
